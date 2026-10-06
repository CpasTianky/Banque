record Rectangle(int height, int width) {
    Rectangle{
        if (height == 0 || width == 0) throw new IllegalArgumentException("Ne peut etre 0");
    }
}
