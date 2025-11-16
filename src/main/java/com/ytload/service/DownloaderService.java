package com.ytload.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DownloaderService {

	public static final Logger log = Logger.getLogger(DownloaderService.class.getName());

	@Async("taskExecutor-")
	public CompletableFuture<File> dowloadVideoViaExecutor(String url, String type) throws Exception {

		File downloadedFile = downloadVideo(url, type);

		return CompletableFuture.completedFuture(downloadedFile);
	}

	public File downloadVideo(String url, String type) throws Exception {
		String ytDlpPath = "C:\\Users\\HP\\Downloads\\yt-dlp.exe";
		String outputPath = "C:\\Users\\HP\\Downloads";

		String[] ytDlpCommandMp4 = { ytDlpPath, "-f", "bestvideo+bestaudio", "--merge-output-format", "mp4",
				"--restrict-filenames", "-o", outputPath + "/%(title)s.%(ext)s", url };

		String[] ytDlpCommandMp3 = { ytDlpPath, "-f", "bestaudio", "--audio-format", "mp3", "--restrict-filenames",
				"-o", outputPath + "/%(title)s.%(ext)s", url };

		List<String> cmdList = null;

		if (type.equalsIgnoreCase("mp3")) {

			cmdList = Arrays.asList(ytDlpCommandMp3).stream().toList();
		} else if (type.equalsIgnoreCase("mp4")) {
			cmdList = Arrays.asList(ytDlpCommandMp4).stream().toList();
		} else {
			throw new Exception("Invalid type");
		}

		ProcessBuilder processBuilder = new ProcessBuilder(cmdList);
		processBuilder.redirectErrorStream(true);

		Process process = processBuilder.start();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				log.info(line);
			}
		}

		int exitCode = process.waitFor();
		if (exitCode != 0) {
			throw new IOException("yt-dlp command failed with exit code " + exitCode);
		}

		Path downloadedFile = Files.list(Paths.get(outputPath)).filter(Files::isRegularFile)
				.max(Comparator.comparingLong(file -> file.toFile().lastModified()))
				.orElseThrow(() -> new IOException("No video file found in output directory."));

		log.info("Downloaded video file: " + downloadedFile.getFileName());
		return downloadedFile.toFile();
	}
}
