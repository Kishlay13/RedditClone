package com.RedditClone.RedditClone.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.RedditClone.RedditClone.dto.AuthenticationResponse;
import com.RedditClone.RedditClone.dto.LoginRequest;
import com.RedditClone.RedditClone.dto.RegisterRequest;
import com.RedditClone.RedditClone.exceptions.GenericException;
import com.RedditClone.RedditClone.exceptions.NonVerifiedTokenException;
import com.RedditClone.RedditClone.model.NotificationEmail;
import com.RedditClone.RedditClone.model.User;
import com.RedditClone.RedditClone.model.VerificationToken;
import com.RedditClone.RedditClone.repository.UserRepository;
import com.RedditClone.RedditClone.repository.VerificationTokenRepository;
import com.RedditClone.RedditClone.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;

	@Transactional
	public void signUp(RegisterRequest registerRequest) {
		User user = new User();
		user.setEnabled(false);
		user.setUserName(registerRequest.getUserName());
		user.setCreatedDate(Instant.now());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

		userRepository.save(user);
		String token = generateVerificationToken(user);
		mailService.sendMail(new NotificationEmail("Please Activate your Account", user.getEmail(),
				"Thank you for signing up to Spring Reddit, "
						+ "please click on the below url to activate your account : "
						+ "http://localhost:8080/api/auth/accountVerification/" + token));
	}

	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationTokenRepository.save(verificationToken);
		return token;

	}

	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(() -> new NonVerifiedTokenException("Invalid Token"));
		getUserAndEnable(verificationToken.get());

	}

	@Transactional
	public void getUserAndEnable(VerificationToken verificationToken) {
		String userName = verificationToken.getUser().getUserName();
		User user = userRepository.findByUserName(userName)
				.orElseThrow(() -> new GenericException("User Not Found" + userName));
		user.setEnabled(true);
		userRepository.save(user);
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		return new AuthenticationResponse(token, loginRequest.getUsername());

	}

}
