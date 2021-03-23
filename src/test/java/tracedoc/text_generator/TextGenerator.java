package tracedoc.text_generator;

import java.util.Arrays;
import java.util.List;

public class TextGenerator {
    // Набор слов из которых генерируется фраза
    private final List<String> noun = Arrays.asList("яблоко", "машина", "телефон", "собака", "трава", "пчела", "дерево");
    private final List<String> adjective = Arrays.asList("красное", "быстрый", "старый", "злой", "высокий", "желтый", "красивый");
    private final List<String> verb = Arrays.asList("съел", "едет", "звонит", "лает", "растёт", "летит", "цветёт");
    private final List<String> preposition = Arrays.asList("и", "за", "в", "на", "под", "к", "под");

    public String generatedString(int count){
        // int count - это количество сгенерированных фраз и столько раз запускаем цикл
        // При каждом прохождении цикла к result добавляется новая фраза
        String result = "";
        for (int i = 0; i < count; i++){
            result = result+" "
                    +noun.get((int)(Math.random()*noun.size()))+" "
                    +adjective.get((int)(Math.random()*noun.size()))+" "
                    +verb.get((int)(Math.random()*noun.size()))+" "
                    +preposition.get((int)(Math.random()*noun.size()));
        }
        // После окончания цикла возвращаем сгенерированный текст
        return result;
    }
}
