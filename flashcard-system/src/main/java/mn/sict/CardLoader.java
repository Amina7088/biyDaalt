package mn.sict;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class CardLoader {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Map<String, List<Card>> loadAll(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new HashMap<String, List<Card>>();
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, List<Card>>>(){}.getType();
            Map<String, List<Card>> data = gson.fromJson(reader, type);
            return (data != null) ? data : new HashMap<String, List<Card>>();
        } catch (IOException e) {
            return new HashMap<String, List<Card>>();
        }
    }

    public void saveAll(String filePath, Map<String, List<Card>> allDecks) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(allDecks, writer);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }
}