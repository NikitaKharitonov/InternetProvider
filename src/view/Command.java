package view;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    GET_TARIFF("^get (Internet|Television|Phone) id=([0-9]+)$"),
    GET_USER("^get User id=([0-9]+)$"), //--------
    GET_TARIFFS("^get (Internets|Televisions|Phones)$"),
    GET_SERVICES("^get Services$"),

    CHANGE_USER("^set User id=([0-9]+)( name=([а-яА-Я ]+))?( phone=(\\+7[0-9]{10}))?( email=([A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}))?$"),
    CHANGE_INTERNET("^set Internet id=([0-9]+)( name=([а-яА-Я ]+))?( speed=([0-9]+))?( antivirus=(true|false))?( connectionType=(ADSL|DialUp|ISDN|Cable|Fiber))?$"),
    CHANGE_TELEVISION("^set Television id=([0-9]+)( name=([а-яА-Я ]+))?( numberOfChannels=([0-9]+))?$"),
    CHANGE_PHONE("^set Phone id=([0-9]+)( name=([а-яА-Я ]+))?( callsMinCount=([0-9]+))?( smsCount=([0-9]+))?$"),

    SET_USER_TARIFF("^set User id=([0-9]+) service=(Internet|Television|Phone) idNewService=([0-9]+)$"),
    CHANGE_USER_INTERNET("^set User id=([0-9]+) service=Internet( speed=([0-9]+))?( antivirus=(true|false))?( connectionType=(ADSL|DialUp|ISDN|Cable|Fiber))?$"),
    CHANGE_USER_PHONE("^set User id=([0-9]+) service=Phone( callsMinCount=([0-9]+))?( smsCount=([0-9]+))?$"),
    CHANGE_USER_TELEVISION("^set User id=([0-9]+) service=Television( numberOfChannels=([0-9]+))"),

    CREATE_USER("^create User name=([а-яА-Я ]+) phone=(\\+7[0-9]{10}) email=([A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4})$"),
    CREATE_INTERNET("^create Internet name=([а-яА-Я0-9 ]+) speed=([0-9]+) antivirus=(true|false) connectionType=(ADSL|DialUp|ISDN|Cable|Fiber)$"),
    CREATE_PHONE("^create Phone name=([а-яА-Я0-9 ]+) callsMinCount=([0-9]+) smsCount=([0-9]+)$"),
    CREATE_TELEVISION("^create name=([а-яА-Я0-9 ]+) Television numberOfChannels=([0-9]+)$"),

    DELETE_USER("^delete User id=([0-9]+)$"),
    DELETE_TARIFF_FROM_USER("^delete User id=([0-9]+) service=(Internet|Television|Phone)$"),
    DELETE_TARIFF("^delete (Internet|Television|Phone) id=([0-9]+)$");


    private final String regex;
    private String rawCommand;

    Command(final String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return this.regex;
    }

//    public String getRawCommand() {
//        return this.rawCommand;
//    }

//    public void setRawCommand(final String rawCommand) {
//        this.rawCommand = rawCommand;
//    }

    public static Optional<Command> parseCommand(final String rawString) {
        Matcher matcher;
        for (final Command nextCommand : values()) {
            matcher = Pattern.compile(nextCommand.getRegex()).matcher(rawString);
            if (matcher.find()) {
                return Optional.of(nextCommand);
            }
        }
        return Optional.empty();
    }

}

