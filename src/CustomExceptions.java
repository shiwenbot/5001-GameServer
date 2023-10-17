public class CustomExceptions {
    public static class AnimalExistsException extends Exception{
        public AnimalExistsException(String message) {
            super(message);
        }
    }
    public static class CreatureExistsException extends Exception{
        public CreatureExistsException(String message) {
            super(message);
        }
    }
}
