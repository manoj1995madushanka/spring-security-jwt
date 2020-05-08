package com.example.springjwt.resource;

import com.example.springjwt.models.AuthenticationRequest;
import com.example.springjwt.models.AuthenticationResponse;
import com.example.springjwt.service.MyUserDetailService;
import com.example.springjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeResource{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @RequestMapping("/")
    public String home(){
        return ("<h1>Welcome</h1>");
    }

    @RequestMapping(value = "/authenticate", method= RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername()
                            ,authenticationRequest.getPassword())
            );
        }catch(BadCredentialsException e){
            throw new Exception("incorrect username or password "+e);
        }
        final UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    /*@GetMapping({"/user"})
    public String user(){
        return ("<h1>Welcome USER</h1>");
    }

    @GetMapping({"/admin"})
    public String admin(){
        return ("<h1>Welcome ADMIN</h1>");
    }*/
}