package controller;

import model.Model;
import view.View;

import java.util.function.Consumer;

public class Controller {

    private Model model;
    private View view;

    private class Handler implements Consumer{
        @Override
        public void accept(Object o) {
            //Обработка запроса
            String type = view.getCommandType();

        }
    }

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        //Установка обработчика запроса
        this.view.setHandler(new Handler());
    }

}
