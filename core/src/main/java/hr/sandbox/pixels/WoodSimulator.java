package hr.sandbox.pixels;

import hr.sandbox.pixels.GameEngine.Materials;



public class WoodSimulator extends AbstractSimulator{


    public WoodSimulator(Materials[][] grid, int width, int height) {
        super(grid, width, height);
    }

    public void dispose(){

    }

    @Override
    protected void update() {


    }




    public void resize(int width, int height) {
        this.width = width;
        this.height = height;


    }

}
