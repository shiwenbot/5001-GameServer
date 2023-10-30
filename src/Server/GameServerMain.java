package Server;

import Animal.Animal;
import Board.Coordinate;
import Board.Game;
import Board.Spell;
import Creature.*;

import javax.json.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static Board.Game.*;

public class GameServerMain {
    public static long seed;
    public static void main(String[] args) {
        seed = 1;
        int port = 8088; // 服务器端口
        Game game = Game.getInstance(seed);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());

                // 处理请求
                handleRequest(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket clientSocket) {
        Game game = Game.getInstance(seed);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // 读取请求行
            String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);

            // 解析请求方法和路径
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            // 处理不同的请求
            if (method.equals("GET") && path.equals("/")) {
                // 处理 GET / 请求
                sendResponse(out, 200, "{\"status\": \"ok\"}");
            } else if (method.equals("GET") && path.equals("/game")) {
                // 处理 GET /game 请求
                // 这里需要返回当前游戏状态的 JSON 数据
                String gameData = getGameData(); // 你需要实现这个方法
                sendResponse(out, 200, gameData);
            } else if (method.equals("POST") && path.equals("/game")) {
                // 处理 POST /game 请求
                // 读取请求体
                int contentLength = getContentLength(in);
                String requestBody = readRequestBody(in, contentLength);

                // 处理请求体，你需要实现这部分逻辑
                String newGameData = processGameAction(requestBody); // 你需要实现这个方法
                sendResponse(out, 200, newGameData);
            } else if (method.equals("POST") && path.equals("/reset")) {
                // 处理 POST /reset 请求
                // 重置游戏并返回新的游戏状态
                String newGameData = resetGame(); // 你需要实现这个方法
                sendResponse(out, 200, newGameData);
            } else if (method.equals("OPTIONS")) {
                // 处理 OPTIONS 请求
                // 发送 CORS 头信息
                out.println("HTTP/1.1 204 No Content");
                out.println("Access-Control-Allow-Origin: *");
                out.println("Access-Control-Allow-Methods: *");
                out.println("Access-Control-Allow-Headers: *");
                out.println("Access-Control-Max-Age: 86400");
            } else {
                // 不支持其他请求类型
                sendResponse(out, 501, "Not Implemented");
            }
            // 关闭连接
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendResponse(PrintWriter out, int statusCode, String body) {
        // 发送响应头
        out.println("HTTP/1.1 " + statusCode);
        out.println("Content-Type: application/json");
        out.println("Access-Control-Allow-Origin: *");
        out.println("Access-Control-Allow-Methods: *");
        out.println("Access-Control-Allow-Headers: *");
        out.println("Access-Control-Max-Age: 86400");
        out.println();

        // 发送响应体
        out.println(body);
    }

    private static int getContentLength(BufferedReader in) throws IOException {
        String line;
        int contentLength = 0;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }
        return contentLength;
    }

    private static String readRequestBody(BufferedReader in, int contentLength) throws IOException {
        char[] buffer = new char[contentLength];
        in.read(buffer, 0, contentLength);
        return new String(buffer);
    }

    // 实现获取游戏状态的方法
    private static String getGameData() {
        Game game = Game.getInstance(1);
        //这个map存储了所有的有生物的坐标，如果一个格子有很多object怎么办？
        ArrayList<Coordinate> locationList = new ArrayList<>();
        for (Coordinate coordinate : objectPosition.values()) {
            locationList.add(coordinate);
        }

        JsonArrayBuilder board = Json.createArrayBuilder();
        //把board框架搭起来
        for (int row = 0; row < 20; row++) {
            JsonArrayBuilder rowBoard = Json.createArrayBuilder();
             for (int col = 0; col < 20; col++) {
                //如果当前坐标存在于map说明有生物
                if(hasObject(row, col, locationList)){
                    Animal animal = isAnimal(row, col);
                    Creature creature = isCreature(row, col);
                    //需要考虑是否同时存在动物和spell
                    if (animal != null && creature != null){
                        JsonObjectBuilder cellDetailBuilder1 = Json.createObjectBuilder();
                        JsonObjectBuilder cellDetailBuilder2 = Json.createObjectBuilder();
                        cellDetailBuilder1.add("name", animal.getName())
                                .add("type", "Animal")
                                .add("description", animal.getDescription())
                                .add("life", animal.getLifePoints());
                        JsonArrayBuilder spellDetails = Json.createArrayBuilder();
                        for (Spell spell : spells){
                            if (animal.getSpells().containsKey(spell)){
                                spellDetails.add(Json.createObjectBuilder()
                                        .add("name", spell.getType())
                                        .add("description", spell.getDescription())
                                        .add("amount", animal.getSpells().get(spell)));
                            }
                        }
                        cellDetailBuilder1.add("spells", spellDetails);

                        cellDetailBuilder2.add("name", creature.name)
                                .add("type", "Creature")
                                .add("shortName", creature.getShortName())
                                .add("description", creature.getDescription())
                                .add("attack", creature.getAttack())
                                .add("confused", false)
                                .add("charmed", Json.createArrayBuilder());
                        rowBoard.add(Json.createArrayBuilder().add(cellDetailBuilder1).add(cellDetailBuilder2));
                    }else if(animal != null){
                        JsonObjectBuilder cellDetailBuilder = Json.createObjectBuilder();
                        cellDetailBuilder.add("name", animal.getName())
                                .add("type", "Animal")
                                .add("description", animal.getDescription())
                                .add("life", animal.getLifePoints());
                        JsonArrayBuilder spellDetails = Json.createArrayBuilder();
                        for (Spell spell : spells){
                            if (animal.getSpells().containsKey(spell)){
                                spellDetails.add(Json.createObjectBuilder()
                                        .add("name", spell.getType())
                                        .add("description", spell.getDescription())
                                        .add("amount", animal.getSpells().get(spell)));
                            }
                        }
                        cellDetailBuilder.add("spells", spellDetails);
                        rowBoard.add(Json.createArrayBuilder().add(cellDetailBuilder));
                    }else if(creature != null && game.board[row][col].isVisible()){
                        JsonObjectBuilder cellDetailBuilder = Json.createObjectBuilder();
                        cellDetailBuilder.add("name", creature.name)
                                .add("type", "Creature")
                                .add("shortName", creature.getShortName())
                                .add("description", creature.getDescription())
                                .add("attack", creature.getAttack())
                                .add("confused", false)
                                .add("charmed", Json.createArrayBuilder());
                        rowBoard.add(Json.createArrayBuilder().add(cellDetailBuilder));
                    } else {
                        rowBoard.add(Json.createArrayBuilder());
                    }
                }
                else {
                    rowBoard.add(Json.createArrayBuilder());
                }
            }
            board.add(rowBoard);
        }

        String statusMessage = errorMessage != null ? errorMessage : "The last move was successful";
        String turnType = null;
        Animal animal = null;
        for (Animal a : animals){
            if (a.getName().equals(moveOrder[game.turn])){
                animal = a;
            }
        }
        Animal preAnimal = findPreviousAnimal(animal);

        if (preAnimal.isSpellable() && !preAnimal.isMoveable()){
            turnType = "spell";
        } else turnType = "move";

        String currentAnimalTurn = null;
        if (turnType.equals("spell")){
            currentAnimalTurn = preAnimal.getName();
        }else {
            currentAnimalTurn = moveOrder[game.turn];
        }

        String nextAnimalTurn = getNextAnimal(currentAnimalTurn);

        JsonObjectBuilder gameBuilder = Json.createObjectBuilder()
                .add("board", board)
                .add("gameOver", false)
                .add("currentAnimalTurn", currentAnimalTurn)
                .add("nextAnimalTurn", nextAnimalTurn);
                if(statusMessage == "The last move was successful"){
                    gameBuilder.add("status", statusMessage);
                }else{
                    gameBuilder.add("status", "The last move was invalid.").add("extendedStatus", errorMessage);
                    errorMessage = null;
                }
        JsonObject gameJson = gameBuilder.build();
        String gameString = gameJson.toString();
        //System.out.println(gameString);
        return gameString;
    }

    private static boolean hasObject(int row, int col, ArrayList<Coordinate> locationList){
        for (Coordinate coordinate: locationList) {
            if (coordinate.getRow() == row && coordinate.getCol() == col)
                return true;
        }
        return false;
    }
    private static String findNextAnimal(int turn, String[] moveOrder){
        if (moveOrder[turn].equals("Badger")) return "Rabbit";
        return moveOrder[turn + 1];
    }
    private static Animal isAnimal(int row, int col){
        for (Animal animal: animals) {
            if (objectPosition.get(animal).getRow() == row && objectPosition.get(animal).getCol() == col){
                return animal;
            }
        }
        return null;
    }

    private static Creature isCreature(int row, int col){
        for (Creature creature: creatures) {
            if(objectPosition.get(creature).getRow() == row && objectPosition.get(creature).getCol() == col){
                return creature;
            }
        }
        return null;
    }

    // 实现处理游戏动作的方法
    private static String processGameAction(String requestBody) {
        Game game = Game.getInstance(seed);
        // 你需要实现这个方法，根据请求体的内容处理游戏动作，返回新的游戏状态的 JSON 数据
        // 例如: return "{\"gameState\": \"updated\"}";
        try{
            // 解析请求体为 JSON 对象
            JsonObject jsonRequest = Json.createReader(new StringReader(requestBody)).readObject();
            String action = jsonRequest.getString("action");
            // 提取请求中的动作、动物和目标坐标
            if (action.equals("move")){
                String animalName = jsonRequest.getString("animal");
                Animal animal = new Animal("tmp");
                for (Animal a: animals) {
                    if(a.getName().equals(animalName)) animal = a;
                }
                JsonObject toSquare = jsonRequest.getJsonObject("toSquare");
                int newRow = toSquare.getInt("row");
                int newCol = toSquare.getInt("col");
                game.moveAnimal(animal, objectPosition.get(animal).getRow(), objectPosition.get(animal).getCol(), newRow, newCol);
            } else if (action.equals("spell")) {
                String animalName = jsonRequest.getString("animal");
                String spellName = jsonRequest.getString("spell");
                //获取动物和法术
                Animal animal = null;
                Spell spell = null;
                for (Animal a: animals) {
                    if(a.getName().equals(animalName)) animal = a;
                }
                for (Spell s : spells){
                    if (s.getType().equals(spellName)) spell = s;
                }
                game.castSpell(animal, spell);
            }
            String newGameData = getGameData();
            return newGameData;
        }catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Invalid request\"}"; // 处理请求出错时返回错误信息
        }
    }


    // 实现重置游戏的方法
    private static String resetGame() {
        // 你需要实现这个方法，重置游戏并返回新的游戏状态的 JSON 数据
        // 例如: return "{\"gameState\": \"reset\"}";
        Game game = Game.getInstance(seed);
        String newGameData = getGameData();
        return newGameData;
    }
}