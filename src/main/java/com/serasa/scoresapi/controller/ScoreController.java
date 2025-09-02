package com.serasa.scoresapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.serasa.scoresapi.service.ScoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/scores")
@SecurityRequirement(name = "bearerAuth")
public class ScoreController {

	private final ScoreService scoreService;

	public ScoreController(ScoreService scoreService) {
		this.scoreService = scoreService;
	}

	@PostMapping("/pessoas/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Registrar score", description = "Registra um novo score para uma pessoa")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Score registrado com sucesso"),
			@ApiResponse(responseCode = "403", description = "Acesso negado") })
	public ResponseEntity<Void> registrarScore(@PathVariable Long id, @RequestParam Integer score) {
		scoreService.registrarScore(id, score);
		return ResponseEntity.noContent().build();
	}
}
