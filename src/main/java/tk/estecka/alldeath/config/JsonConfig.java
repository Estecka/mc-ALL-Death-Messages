package tk.estecka.alldeath.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.slf4j.Logger;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class JsonConfig 
{
	public final String filename;
	public final Path path;
	public final File file;

	private final Logger logger;

	private ModContainer	mod = null;
	private Path pathToDefault = null;

	public	JsonConfig(String filename, String modId, Logger logger){
		this.filename = filename;
		FabricLoader loader = FabricLoader.getInstance();
		this.logger = logger;

		this.path = loader.getConfigDir().resolve(filename);
		this.file = path.toFile();

		var optMod = loader.getModContainer(modId);
		if (optMod.isEmpty())
			this.logger.error("{} is not a valid mod id", modId);
		else {
			this.mod = optMod.get();
			var optPath = this.mod.findPath("config/"+filename);
			if (optPath.isPresent())
				this.pathToDefault = optPath.get();
		}
	}

	public JsonElement	GetOrCreateJsonFile() throws IOException{
		if (!this.file.exists()){
			CreateFileFromDefault();
			this.logger.info("Automatically created new config file {}", filename);
		}
		return GetJsonFromFile();
	}

	public JsonElement	GetJsonFromFile() throws FileNotFoundException {
		FileInputStream cin = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(cin);
		JsonElement json = JsonParser.parseReader(reader);
		return json;
	}

	public boolean	CreateFileFromDefault() throws IOException{
		if (pathToDefault == null) {
			this.logger.error("No default file was found for {}", filename);
			throw new FileNotFoundException();
		}

		Path src = this.pathToDefault;
		Path dst = this.path;
		Files.copy(src, dst, StandardCopyOption.COPY_ATTRIBUTES);
		return true;
	}
}
