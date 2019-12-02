package view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {
    GET_USER("^get User id=(\\d+)$"),
    GET_USER_SERVICE("^get User id=(\\d+) service=(Internet|Phone|Television)$"),
    GET_SERVICE("^get (Internet|Phone|Television) id=(\\d+)$"),
    GET_SERVICES("^get (Internet|Phone|Television)s$"),

    CREATE_USER("^create User name=([\\w ]+) phone=(\\+7\\d{10}) email=([\\w\\d._%-]+@[\\w\\d.-]+\\.\\w{2,4})$"),
    CREATE_INTERNET("^create Internet name=([\\w\\d ]+) speed=(\\d+) antivirus=(true|false) connectionType=(ADSL|DialUp|ISDN|Cable|Fiber)$"),
    CREATE_PHONE("^create Phone name=([\\w\\d ]+) callsMinCount=(\\d+) smsCount=(\\d+)$"),
    CREATE_TELEVISION("^create Television name=([\\w\\d ]+) numberOfChannels=(\\d+)$"),

    CHANGE_USER("^set User id=(\\d+)( name=([\\w ]+))?( phone=(\\+7\\d{10}))?( email=([\\w\\d._%-]+@[\\w\\d.-]+\\.\\w{2,4}))?$"),
    CHANGE_USER_INTERNET("^set User id=(\\d+) service=Internet( name=([\\w\\d ]+))?( speed=(\\d+))?( antivirus=(true|false))?( connectionType=(ADSL|DialUp|ISDN|Cable|Fiber))?$"),
    CHANGE_USER_PHONE("^set User id=(\\d+) service=Phone( name=([\\w\\d ]+))?( callsMinCount=(\\d+))?( smsCount=(\\d+))?$"),
    CHANGE_USER_TELEVISION("^set User id=(\\d+) service=Television( name=([\\w\\d ]+))?( numberOfChannels=(\\d+))?$"),

    CHANGE_INTERNET("^set Internet id=(\\d+)( name=([\\w\\d ]+))?( speed=(\\d+))?( antivirus=(true|false))?( connectionType=(ADSL|DialUp|ISDN|Cable|Fiber))?$"),
    CHANGE_PHONE("^set Phone id=(\\d+)( name=([\\w\\d ]+))?( callsMinCount=(\\d+))?( smsCount=(\\d+))?$"),
    CHANGE_TELEVISION("^set Television id=(\\d+)( name=([\\w\\d ]+))?( numberOfChannels=(\\d+))?$"),

    SET_USER_SERVICE("^set User id=(\\d+) serviceID=(\\d+)$"),

    DELETE_USER("^delete User id=(\\d+)$"),
    DELETE_SERVICE("^delete (Internet|Television|Phone) id=(\\d+)$"),
    DELETE_USER_SERVICE("^delete User id=(\\d+) service=(Internet|Television|Phone)$");

    private final String regex;

    Command(final String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return this.regex;
    }

    public static Command parseCommand(final String rawString) {
        Matcher matcher;
        for (final Command nextCommand : values()) {
            matcher = Pattern.compile(nextCommand.getRegex(), Pattern.CASE_INSENSITIVE).matcher(rawString);
            if (matcher.find()) {
                return nextCommand;
            }
        }
        return null;
    }
}

