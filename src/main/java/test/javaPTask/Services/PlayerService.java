package test.javaPTask.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import test.javaPTask.Entities.GlobalVariables;
import test.javaPTask.Entities.Operator;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;
@AllArgsConstructor
@NoArgsConstructor
public class PlayerService {

    private GlobalVariables globalVariables;

    public List<Operator> loadOperatorList(Path filePath) {
        try (FileReader reader = new FileReader(filePath.toFile())) {
            Gson gson = new Gson();
            Type playerListType = new TypeToken<List<Operator>>(){}.getType();
            return gson.fromJson(reader, playerListType);
        } catch (IOException e) {
            getLogger().severe("Failed to load player data: " + e.getMessage());
            return List.of();
        }
    }
    public boolean isOperator(String currentPlayerUUID){
        getLogger().info("Op list UUID:"+globalVariables.getOperatorList().get(0).getUuid());
        getLogger().info("Current player UUID:"+currentPlayerUUID);
        return globalVariables.getOperatorList().stream().anyMatch(player -> player.getUuid().equals(currentPlayerUUID));
    }
}
