package view;

import controller.*;
import model.User;
import model.services.Internet;
import model.services.Phone;
import model.services.Service;
import model.services.Television;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Map;

public interface View {

    void run() throws IOException, FailedOperation, CloneNotSupportedException;
}

