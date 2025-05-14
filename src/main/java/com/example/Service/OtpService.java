package com.example.Service;

import com.example.DTO.OtpDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class OtpService {
    private final Map<String, OtpDetails> otpStorage = new HashMap<>() ;
    private Map<String, Boolean> verifiedEmails = new HashMap<>();

    Random random=new Random();

    public OtpDetails generate_Otp(String email){
        //generating a 4-digit otp
        int otp = random.nextInt(9999);
        OtpDetails otpDetails = new OtpDetails(otp, LocalDateTime.now().plusMinutes(5)) ;
        otpStorage.put(email, otpDetails) ;
        //{ 'email' : '1234 , 5 minutes '}

        return otpDetails ;
    }

    public boolean validateOTP(String email, String otp) {
        try{
            OtpDetails otpDetails = otpStorage.get(email) ;  // get the value of email (key) present in otpStorage
            if ( otpDetails == null || otpDetails.getExpiry().isBefore(LocalDateTime.now())){
                return false ; // otp got expired || or its null
            }

            boolean isValid = otpDetails.getOtp().equals(Integer.parseInt(otp));
            if(isValid){
                verifiedEmails.put(email,true) ;
                otpStorage.remove(email) ; //removing from the map after verification
            }
            return isValid ;

        }catch (NumberFormatException ex){
            return false ;
        }
    }

    public boolean isEmailVerified(String email){
        return verifiedEmails.getOrDefault(email, false) ;
    }
}
