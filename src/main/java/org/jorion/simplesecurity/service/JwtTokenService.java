package org.jorion.simplesecurity.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

	@Autowired
	private JwtEncoder encoder;

	public String generateToken(Authentication auth) {
		
	    Instant now = Instant.now();
	    String scope = auth.getAuthorities().stream()
	        .map(GrantedAuthority::getAuthority)
	        .collect(Collectors.joining(" "));
	    JwtClaimsSet claims = JwtClaimsSet.builder()
	        .issuer("self")
	        .issuedAt(now)
	        .expiresAt(now.plus(1, ChronoUnit.HOURS))
	        .subject(auth.getName())
	        .claim("scope", scope)
	        .build();

		// var jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build();
		// JwtEncoderParameters.from(jwsHeader, claims);
		var encoderParams = JwtEncoderParameters.from(claims);
		return encoder.encode(encoderParams).getTokenValue();
	}

}
