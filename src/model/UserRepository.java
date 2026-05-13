package model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UserRepository {
	
	private File userFile;
	private Path path;

	public UserRepository() {
		userFile = new File("users.txt");
		path = userFile.toPath();

		try {
			userFile.createNewFile();
			if (userFile.exists()) {
				System.out.println("Users file is ready.");
			} else {
				System.out.println("Users file creation failed.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public User findByUsername(String username) {
		try {
			if (Files.exists(path) && Files.isReadable(path)) {
				List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

				for (String line : lines) {
					String[] parts = line.split(",");

					if (parts.length == 3) {
						String storedRole = parts[0].trim();
						String storedUsername = parts[1].trim();
						String storedPassword = parts[2].trim();

						if (storedUsername.equals(username)) {
							return new User(storedUsername, storedPassword, storedRole);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}