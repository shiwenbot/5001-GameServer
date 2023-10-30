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
        int port = 8088; // �������˿�
        Game game = Game.getInstance(seed);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());

                // ��������
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

            // ��ȡ������
            String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);

            // �������󷽷���·��
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            // ����ͬ������
            if (method.equals("GET") && path.equals("/")) {
                // ���� GET / ����
                sendResponse(out, 200, "{\"status\": \"ok\"}");
            } else if (method.equals("GET") && path.equals("/game")) {
                // ���� GET /game ����
                // ������Ҫ���ص�ǰ��Ϸ״̬�� JSON ����
                String gameData = getGameData(); // ����Ҫʵ���������
                sendResponse(out, 200, gameData);
            } else if (method.equals("POST") && path.equals("/game")) {
                // ���� POST /game ����
                // ��ȡ������
                int contentLength = getContentLength(in);
                String requestBody = readRequestBody(in, contentLength);

                // ���������壬����Ҫʵ���ⲿ���߼�
                String newGameData = processGameAction(requestBody); // ����Ҫʵ���������
                sendResponse(out, 200, newGameData);
            } else if (method.equals("POST") && path.equals("/reset")) {
                // ���� POST /reset ����
                // ������Ϸ�������µ���Ϸ״̬
                String newGameData = resetGame(); // ����Ҫʵ���������
                sendResponse(out, 200, newGameData);
            } else if (method.equals("OPTIONS")) {
                // ���� OPTIONS ����
                // ���� CORS ͷ��Ϣ
                out.println("HTTP/1.1 204 No Content");
                out.println("Access-Control-Allow-Origin: *");
                out.println("Access-Control-Allow-Methods: *");
                out.println("Access-Control-Allow-Headers: *");
                out.println("Access-Control-Max-Age: 86400");
            } else {
                // ��֧��������������
                sendResponse(out, 501, "Not Implemented");
            }
            // �ر�����
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendResponse(PrintWriter out, int statusCode, String body) {
        // ������Ӧͷ
        out.println("HTTP/1.1 " + statusCode);
        out.println("Content-Type: application/json");
        out.println("Access-Control-Allow-Origin: *");
        out.println("Access-Control-Allow-Methods: *");
        out.println("Access-Control-Allow-Headers: *");
        out.println("Access-Control-Max-Age: 86400");
        out.println();

        // ������Ӧ��
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

    // ʵ�ֻ�ȡ��Ϸ״̬�ķ���
    private static String getGameData() {
        Game game = Game.getInstance(1);
        //���map�洢�����е�����������꣬���һ�������кܶ�object��ô�죿
        ArrayList<Coordinate> locationList = new ArrayList<>();
        for (Coordinate coordinate : objectPosition.values()) {
            locationList.add(coordinate);
        }

        JsonArrayBuilder board = Json.createArrayBuilder();
        //��board��ܴ�����
        for (int row = 0; row < 20; row++) {
            JsonArrayBuilder rowBoard = Json.createArrayBuilder();
             for (int col = 0; col < 20; col++) {
                //�����ǰ���������map˵��������
                if(hasObject(row, col, locationList)){
                    Animal animal = isAnimal(row, col);
                    Creature creature = isCreature(row, col);
                    //��Ҫ�����Ƿ�ͬʱ���ڶ����spell
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

    // ʵ�ִ�����Ϸ�����ķ���
    private static String processGameAction(String requestBody) {
        Game game = Game.getInstance(seed);
        // ����Ҫʵ�������������������������ݴ�����Ϸ�����������µ���Ϸ״̬�� JSON ����
        // ����: return "{\"gameState\": \"updated\"}";
        try{
            // ����������Ϊ JSON ����
            JsonObject jsonRequest = Json.createReader(new StringReader(requestBody)).readObject();
            String action = jsonRequest.getString("action");
            // ��ȡ�����еĶ����������Ŀ������
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
                //��ȡ����ͷ���
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
            return "{\"error\": \"Invalid request\"}"; // �����������ʱ���ش�����Ϣ
        }
    }


    // ʵ��������Ϸ�ķ���
    private static String resetGame() {
        // ����Ҫʵ�����������������Ϸ�������µ���Ϸ״̬�� JSON ����
        // ����: return "{\"gameState\": \"reset\"}";
        Game game = Game.getInstance(seed);
        String newGameData = getGameData();
        return newGameData;
    }
}