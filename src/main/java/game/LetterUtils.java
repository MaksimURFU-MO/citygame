package game;

public class LetterUtils {

    public char lastSymbol(String city){
        if(city == null || city.isEmpty())
            throw new IllegalArgumentException("Город не может быть пустой строкой");
        city = city.trim();
        city = city.toLowerCase();
        char lastChar = city.charAt(city.length() - 1);
        boolean end = true;
        int count = 2;
        while (end){
            if ((lastChar == 'ъ' || lastChar == 'ь' || lastChar == 'ы' || lastChar == 'й') && count <= city.length()) {
                lastChar = city.charAt(city.length() - count);
                count++;
            }
            else{
                end = false;
            }
        }
        if(lastChar == 'ё')
            lastChar = 'е';
        return lastChar;
    }



}
