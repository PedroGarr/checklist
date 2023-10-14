package pedrogarr.checklist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pedrogarr.checklist.user.IUserRepository;

@Component
public class FilterTaskAuth extends OncePerRequestFilter  {

    @Autowired 
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

            //Check if we are on the right route 

            var servletPath = request.getServletPath();

            if(servletPath.equals("/tasks/")){
                //Do the authentication of the user

                var authorization = request.getHeader("Authorization");
                var authEncoded = authorization.substring("Basic".length()).trim();

                byte[] authDecode = Base64.getDecoder().decode(authEncoded);

                var authString = new String(authDecode);

                String[] credentials = authString.split(":");
                String username = credentials[0];
                String password = credentials[1];

                //Validate user
                var user = this.userRepository.findByUsername(username);

                if (user == null ){
                    response.sendError(401);
                } else { 
                    //Validate password and if everything is correct continue the flow
                    
                    var passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                    if(passwordVerified.verified){
                        request.setAttribute("idUser",user.getId());
                        filterChain.doFilter(request,response);

                    } else {
                    response.sendError(401); 
                    }               
                }
               
            } else {
                filterChain.doFilter(request,response);
            }
           
            

  
    }

   
    
    
}
