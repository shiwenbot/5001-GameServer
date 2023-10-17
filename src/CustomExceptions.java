public class CustomExceptions {
    public static class AnimalExistsException extends Exception{
        public AnimalExistsException(String message) {
            super(message);
        }
    }
}
