package Perpuskaan.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function; // Tambahan Import Penting

@Component
public class JwtUtils {

    // Kunci Rahasia (Harus panjang dan rahasia, minimal 256 bit)
    private static final String SECRET = "RahasiaNegaraIniSangatPanjangSekaliDanSulitDitebak123456"; 
    private static final long EXPIRATION_TIME = 86400000; // 24 Jam

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // ==========================================
    // 1. GENERATE TOKEN
    // ==========================================
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ==========================================
    // 2. VALIDASI TOKEN (YANG KAMU CARI)
    // ==========================================
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Token valid jika: Username cocok DAN Token belum expired
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // ==========================================
    // 3. HELPER METHODS (YANG KURANG)
    // ==========================================

    // Ambil Username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Ambil Tanggal Expired
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Cek apakah token sudah expired?
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Method Generic untuk ambil data apapun (Claim) dari token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Bongkar Token (Parsing)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}