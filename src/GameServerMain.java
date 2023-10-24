import Animal.Animal;
import Board.Coordinate;
import Board.Game;
import Creature.*;
import Animal.*;


import javax.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static Board.Game.*;

public class GameServerMain {
    public static void main(String[] args) {
        int port = 8088; // 服务器端口
        Game game = Game.getInstance(1);
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
        Game game = Game.getInstance(1);
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
        //Game game = Game.getInstance(1);
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
                    //需要考虑是否同时存在动物和spell
                    if (isAnimal(row, col) != null && isCreature(row, col) != null){
                        JsonObjectBuilder cellDetailBuilder1 = Json.createObjectBuilder();
                        JsonObjectBuilder cellDetailBuilder2 = Json.createObjectBuilder();
                        cellDetailBuilder1.add("name", isAnimal(row, col).getName())
                                .add("type", "Animal")
                                .add("description", isAnimal(row, col).description)
                                .add("life", isAnimal(row, col).lifePoints)
                                .add("spells", Json.createArrayBuilder());
                        cellDetailBuilder2.add("name", isCreature(row, col).name)
                                .add("type", "Creature")
                                .add("shortName", isCreature(row, col).shortName)
                                .add("description", isCreature(row, col).description)
                                .add("attack", isCreature(row, col).attack)
                                .add("confused", false)
                                .add("charmed", Json.createArrayBuilder());
                        rowBoard.add(Json.createArrayBuilder().add(cellDetailBuilder1).add(cellDetailBuilder2));
                    }else if(isAnimal(row, col) != null){
                        JsonObjectBuilder cellDetailBuilder = Json.createObjectBuilder();
                        cellDetailBuilder.add("name", isAnimal(row, col).getName())
                                .add("type", "Animal")
                                .add("description", isAnimal(row, col).getDescription())
                                .add("life", isAnimal(row, col).getLifePoints())
                                .add("spells", Json.createArrayBuilder());
                        rowBoard.add(Json.createArrayBuilder().add(cellDetailBuilder));
                    }else if(isCreature(row, col) != null){
                        JsonObjectBuilder cellDetailBuilder = Json.createObjectBuilder();
                        cellDetailBuilder.add("name", isCreature(row, col).name)
                                .add("type", "Creature")
                                .add("shortName", isCreature(row, col).shortName)
                                .add("description", isCreature(row, col).description)
                                .add("attack", isCreature(row, col).attack)
                                .add("confused", false)
                                .add("charmed", Json.createArrayBuilder());
                        rowBoard.add(Json.createArrayBuilder().add(cellDetailBuilder));
                    }
                }
                else {
                    rowBoard.add(Json.createArrayBuilder());
                }
            }
            board.add(rowBoard);
        }

        JsonObjectBuilder gameBuilder = Json.createObjectBuilder()
                .add("board", board)
                .add("gameOver", false)
                .add("currentAnimalTurn", "Rabbit")
                .add("nextAnimalTurn", "Fox")
                .add("status", "The last move was successful.");
        JsonObject gameJson = gameBuilder.build();
        String gameString = gameJson.toString();
        System.out.println(gameString);
        return gameString;
    }

    private static boolean hasObject(int row, int col, ArrayList<Coordinate> locationList){
        for (Coordinate coordinate: locationList) {
            if (coordinate.getRow() == row && coordinate.getCol() == col)
                return true;
        }
        return false;
    }

    private static Animal isAnimal(int row, int col){
        for (Animal animal: animals) {
            if (objectPosition.get(animal).getRow() == row && objectPosition.get(animal).getCol() == col){
                if (animal instanceof Badger) {
                    return (Badger) animal;
                } else if (animal instanceof Rabbit) {
                    return (Rabbit) animal;
                } else if (animal instanceof Fox) {
                    return (Fox) animal;
                } else if (animal instanceof Deer) {
                    return (Deer) animal;
                } else if (animal instanceof Owl) {
                    return (Owl) animal;
                }
            }
        }
        return null;
    }

    private static Creature isCreature(int row, int col){
        for (Creature creature: creatures) {
            if(objectPosition.get(creature).getRow() == row && objectPosition.get(creature).getCol() == col){
                if (creature instanceof Unicorn) {
                    return (Unicorn) creature;
                } else if (creature instanceof Centaur) {
                    return (Centaur) creature;
                } else if (creature instanceof Dragon) {
                    return (Dragon) creature;
                } else if (creature instanceof Phoenix) {
                    return (Phoenix) creature;
                } else if (creature instanceof Sphinx) {
                    return (Sphinx) creature;
                }
            }
        }
        return null;
    }

    private static void addAnimalDetail(JsonObjectBuilder cellDetailBuilder, Animal animal){
        cellDetailBuilder.add("name", animal.getName())
                .add("type", "Animal")
                .add("description", animal.description)
                .add("life", animal.lifePoints)
                .add("spells", Json.createArrayBuilder());
    }

    private static void addCreatureDetail(JsonObjectBuilder cellDetailBuilder, Creature creature){
        cellDetailBuilder.add("name", creature.name)
                .add("type", "Creature")
                .add("shortName", creature.shortName)
                .add("description", creature.description)
                .add("attack", creature.attack)
                .add("confused", false)
                .add("charmed", Json.createArrayBuilder());
    }

//    private Spell isSpell(int row, int col){
//
//    }




    // 实现处理游戏动作的方法
    private static String processGameAction(String requestBody) {
        // 你需要实现这个方法，根据请求体的内容处理游戏动作，返回新的游戏状态的 JSON 数据
        // 例如: return "{\"gameState\": \"updated\"}";
        return "{\"gameState\": \"updated\"}";
    }

    // 实现重置游戏的方法
    private static String resetGame() {
        // 你需要实现这个方法，重置游戏并返回新的游戏状态的 JSON 数据
        // 例如: return "{\"gameState\": \"reset\"}";
        return "{\"gameState\": \"reset\"}";
    }
}