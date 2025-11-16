package com.ytload.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ytload.dto.RequestDTO;
import com.ytload.service.DownloaderService;

@RestController
public class DownloadController {

	@Autowired
	DownloaderService service;

	@GetMapping("/downloadfile2")
	public ResponseEntity<byte[]> downloadVideoFile(@RequestBody RequestDTO dto) throws IOException {

		File file = null;

		try {
			file = service.downloadVideo(dto.getUrl(), dto.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}

		HttpHeaders http = new HttpHeaders();

		http.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		http.setContentDispositionFormData("attachment", file.getName());

		byte[] allBytes = Files.readAllBytes(file.toPath());

		return new ResponseEntity<byte[]>(allBytes, http, HttpStatus.OK);
	}

	@GetMapping("/downloadfile")
	public CompletableFuture<ResponseEntity<byte[]>> downloadVideoFile2(@RequestBody RequestDTO dto) throws Exception {

		return service.dowloadVideoViaExecutor(dto.getUrl(), "mp4").thenApply(file -> {

			try {
				byte[] allBytes = Files.readAllBytes(file.toPath());

				HttpHeaders header = new HttpHeaders();

				header.setContentDispositionFormData("attachment", file.getName());
				return new ResponseEntity<>(allBytes, header, HttpStatus.OK);

			} catch (Exception e) {

				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		});

	}

}
