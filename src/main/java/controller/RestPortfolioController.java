package controller;

import java.util.List;
import java.util.stream.Collectors;
import java.net.URI;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import authentication.JwtManager;
import portfolio.Portfolio;
import portfolio.PortfolioDao;
import portfolio.PortfolioRegisterService;
import portfolio.PortfolioRequest;

@RestController
@RequestMapping("/api/portfolio/")
public class RestPortfolioController {
	private PortfolioDao portfolioDao;
	private PortfolioRegisterService registerService;
	private JwtManager jwtManager;
	
	public void setPortfolioDao(PortfolioDao portfolioDao) {
		this.portfolioDao = portfolioDao;
	}
	public void setRegisterService(PortfolioRegisterService registerSerivce) {
		this.registerService = registerSerivce;
	}
	public void setJwtManager(JwtManager jwtManager) {
		this.jwtManager = jwtManager;
	}
	
	// 포트폴리오 목록 보기
	@GetMapping()
	public List<Portfolio> portfolio() {
		return portfolioDao.selectAll();
	}
	
	// 특정 포트폴리오 목록 보기
	@GetMapping("/{portfolio_id}")
	public ResponseEntity<Object> portfolio(@PathVariable Long portfolio_id) {
		Portfolio portfolio = portfolioDao.selectById(portfolio_id);
		if (portfolio == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse("no portfolio"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(portfolio);
	}
	
	// 포트폴리오 작성
	@PostMapping()
	public ResponseEntity<Object> newPortfolio(
			@CookieValue("token") String token, @RequestBody PortfolioRequest req, Errors errors, HttpServletResponse response) {
		if (errors.hasErrors()) {
			String errorCodes = errors.getAllErrors()
					.stream()
					.map(error -> error.getCodes()[0])
					.collect(Collectors.joining("."));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("errorCodes = " + errorCodes));		
		}
		final Long user_id = jwtManager.getIdFromToken(token);
		Long newPortfolio_id = registerService.regist(user_id, req);
		URI uri = URI.create("/api/portfolio/" + newPortfolio_id);
		return ResponseEntity.created(uri).build();
	}	
	
}