package com.example.Service;

import com.example.DTO.OtpDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class OtpService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate ;

    private static String OTP_PREFIX = "otp:" ;
    private static String VERIFIED_KEY_PREFIX = "verified:" ;

    Random random=new Random();

    public OtpDetails generate_Otp(String email){
        //generating a 4-digit otp
        int otp = random.nextInt(9999);
        OtpDetails otpDetails = new OtpDetails(otp, LocalDateTime.now().plusMinutes(5)) ;
        redisTemplate.opsForValue().set(OTP_PREFIX+email , otpDetails, Duration.ofMinutes(5)); ;
        //{ 'email' : '1234 , 5 minutes '}

        return otpDetails ;
    }

    public boolean validateOTP(String email, String otp) {
        String key = OTP_PREFIX+email;
        OtpDetails otpDetails = (OtpDetails) redisTemplate.opsForValue().get(key) ;   // get the value of email (key) present in otpStorage

        if ( otpDetails == null || otpDetails.getExpiry().isBefore(LocalDateTime.now())){
                return false ; // otp got expired || or its null
        }

        boolean isValid = otpDetails.getOtp().equals(Integer.parseInt(otp));

        if(isValid){
            redisTemplate.opsForValue().set(VERIFIED_KEY_PREFIX+email, true, Duration.ofMinutes(15));
            redisTemplate.delete(key) ;  //removing from the redis data-storage after verification
        }
        return isValid ;
    }

    public boolean isEmailVerified(String email){
        Boolean isVerified = (Boolean) redisTemplate.opsForValue().get(VERIFIED_KEY_PREFIX+email) ;
        return Boolean.TRUE.equals(isVerified);
    }
}
