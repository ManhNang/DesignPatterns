public class FactoryPatternDemo {
    public static void main(String[] args) {
        ShapeFactory shapeFactory = new ShapeFactory();

        Shape circle = shapeFactory.getShape("CirclE");
        circle.draw();

        Shape square = shapeFactory.getShape("sQuare");
        square.draw();

        Shape rectangle = shapeFactory.getShape("Rectangle");
        rectangle.draw();

        Shape notShape = shapeFactory.getShape("notShape");
    }
}
