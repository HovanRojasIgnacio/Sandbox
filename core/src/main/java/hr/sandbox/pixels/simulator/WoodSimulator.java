package hr.sandbox.pixels.simulator;

import hr.sandbox.pixels.GameEngine.Materials;



public class WoodSimulator extends AbstractSimulator{


    public WoodSimulator(Materials[][] grid, int width, int height) {
        super(grid, width, height);
    }

    @Override
    public Materials getMaterial() {
        return Materials.wood;
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
