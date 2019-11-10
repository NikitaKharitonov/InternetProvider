package controller;

import model.Model;
import util.Procedure;
import view.View;

public class Controller {

    private Model model;
    private View view;

    private class Handler implements Procedure {
        @Override
        public void execute() {

        }
    }

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        //Установка обработчика запроса
        this.view.setHandler(new Handler());
    }

}
