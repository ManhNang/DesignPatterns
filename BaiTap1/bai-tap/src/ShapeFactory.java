public class ShapeFactory {
    public Shape getShape(String typeShape) {
        switch (typeShape.toLowerCase()) {
            case "circle":
                return new Circle();
            case "square":
                return new Square();
            case "rectangle":
                return new Rectangle();
            default:
                System.out.println("No shape exist!");
                return null;
        }
    }
}
