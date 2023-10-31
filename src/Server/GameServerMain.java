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
import java.util.Random;

import static Board.Game.*;

public class GameServerMain {
    public static long seed;
    public static int port;

    /**
     * The main method of the Game Server.
     * Initializes the server, listens for incoming connections, and handles requests.
     *
     * Command line arguments containing the seed and port number.
     */
    public static void main(String[] args) {
        //seed = Integer.parseInt(args[0]);
        //port = Integer.parseInt(args[0]);
        seed = 1;
        port = 8088;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());
                handleRequest(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Handles an incoming client request.
    private static void handleRequest(Socket clientSocket) {
        Game game = Game.getInstance(seed);
        String finalGameData = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read the request line
            String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);

            // Parse the request method and path
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            // Handle different types of requests
            if (method.equals("GET") && path.equals("/")) {
                // Handle GET / request
                sendResponse(out, 200, "{\"status\": \"ok\"}");
            } else if (method.equals("GET") && path.equals("/game")) {
                // Handle GET /game request
                String gameData = getGameData();
                sendResponse(out, 200, gameData);
            }  else if (method.equals("POST") && path.equals("/game")) {
                // Handle POST /game request
                int contentLength = getContentLength(in);
                String requestBody = readRequestBody(in, contentLength);

                String newGameData = processGameAction(requestBody);
                if (game.gameOver()) {
                    finalGameData = newGameData;
                }
                sendResponse(out, 200, game.gameOver() ? finalGameData : newGameData);
            } else if (method.equals("POST") && path.equals("/reset")) {
                String newGameData = resetGame();
                sendResponse(out, 200, newGameData);
            } else if (method.equals("OPTIONS")) {
                // CORS header
                out.println("HTTP/1.1 204 No Content");
                out.println("Access-Control-Allow-Origin: *");
                out.println("Access-Control-Allow-Methods: *");
                out.println("Access-Control-Allow-Headers: *");
                out.println("Access-Control-Max-Age: 86400");
            } else {
                // Unsupported request type
                sendResponse(out, 501, "Not Implemented");
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an HTTP response to the client.
     *
     * @param out        PrintWriter for sending the response.
     * @param statusCode HTTP status code.
     * @param body       Response body in JSON format.
     */
    private static void sendResponse(PrintWriter out, int statusCode, String body) {
        out.println("HTTP/1.1 " + statusCode);
        out.println("Content-Type: application/json");
        out.println("Access-Control-Allow-Origin: *");
        out.println("Access-Control-Allow-Methods: *");
        out.println("Access-Control-Allow-Headers: *");
        out.println("Access-Control-Max-Age: 86400");
        out.println();

        out.println(body);
    }

    // Extracts the content length from the request headers.
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

    // Read the request body from the client.
    private static String readRequestBody(BufferedReader in, int contentLength) throws IOException {
        char[] buffer = new char[contentLength];
        in.read(buffer, 0, contentLength);
        return new String(buffer);
    }

    /**
     * This method retrieves the game data in JSON format.
     *
     * @return A JSON string representing the current game state.
     */
    private static String getGameData() {
        Game game = Game.getInstance(seed);

        ArrayList<Coordinate> locationList = new ArrayList<>();
        for (Coordinate coordinate : objectPosition.values()) {
            locationList.add(coordinate);
        }

        JsonArrayBuilder board = Json.createArrayBuilder();
        for (int row = 0; row < 20; row++) {
            JsonArrayBuilder rowBoard = Json.createArrayBuilder();
             for (int col = 0; col < 20; col++) {
                if(hasObject(row, col, locationList)){
                    Animal animal = isAnimal(row, col);
                    Creature creature = isCreature(row, col);

                    // If the square has animal and creature at the same time
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
                    }
                    // only have animal
                    else if(animal != null){
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
                    }
                    //only has creature and this square is visibile
                    else if(creature != null && game.board[row][col].isVisible()){
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

        String statusMessage = errorMessage != null ? errorMessage : "The last action was successful";
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
        boolean gameOver = game.gameOver();

        String nextAnimalTurn = getNextAnimal(currentAnimalTurn);

        JsonObjectBuilder gameBuilder = Json.createObjectBuilder()
                .add("board", board)
                .add("gameOver", gameOver)
                .add("currentAnimalTurn", currentAnimalTurn)
                .add("turnType", turnType)
                .add("nextAnimalTurn", nextAnimalTurn);
                if(statusMessage == "The last action was successful"){
                    gameBuilder.add("status", statusMessage);
                }else{
                    gameBuilder.add("status", "The last move was invalid.").add("extendedStatus", errorMessage);
                }
        JsonObject gameJson = gameBuilder.build();
        String gameString = gameJson.toString();
        return gameString;
    }


    // This method checks if there is any animals and creatures in the square.
    private static boolean hasObject(int row, int col, ArrayList<Coordinate> locationList){
        for (Coordinate coordinate: locationList) {
            if (coordinate.getRow() == row && coordinate.getCol() == col)
                return true;
        }
        return false;
    }

    // This method checks if the object is an animal, if so the animal would be return
    private static Animal isAnimal(int row, int col){
        for (Animal animal: animals) {
            if (objectPosition.get(animal).getRow() == row && objectPosition.get(animal).getCol() == col){
                return animal;
            }
        }
        return null;
    }

    // This method checks if the object is a creature, if so the creature would be return
    private static Creature isCreature(int row, int col){
        for (Creature creature: creatures) {
            if(objectPosition.get(creature).getRow() == row && objectPosition.get(creature).getCol() == col){
                return creature;
            }
        }
        return null;
    }

    // This method processes game actions (move and cast spell)
    private static String processGameAction(String requestBody) {
        Game game = Game.getInstance(seed);
        try{
            JsonObject jsonRequest = Json.createReader(new StringReader(requestBody)).readObject();
            String action = jsonRequest.getString("action");
            // move
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
            }
            // cast spell
            else if (action.equals("spell")) {
                String animalName = jsonRequest.getString("animal");
                String spellName = jsonRequest.getString("spell");
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
            return "{\"error\": \"Invalid request\"}";
        }
    }

    // resets the game
    private static String resetGame() {
        instance = null;
        Random r = new Random();
        seed = r.nextInt();
        Game.getInstance(seed);
        String newGameData = getGameData();
        return newGameData;
    }
}