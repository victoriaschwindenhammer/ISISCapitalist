/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ISISCapitalist;

import generated.PallierType;
import generated.ProductType;
import generated.World;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Johana Dahan
 */
@RestController
@RequestMapping("adventureisis/generic")
@CrossOrigin
public class Webservice {

    Services services;

    public Webservice() {
        services = new Services();
    }

    @GetMapping(value = "world", produces = {"application/xml", "application/json"})
    public ResponseEntity<World> getWorld(@RequestHeader(value = "X-User", required = false) String username) {
        World world = services.getWorld(username);
        return ResponseEntity.ok(world);
    }

    @PUT
    @PutMapping("product")
    public void putProduct(@Context HttpServletRequest request, ProductType product) {
        services.updateProduct(request.getHeader("X-User"), product);
    }
  
    @PUT
    @PutMapping("manager")
    public void putManager(@Context HttpServletRequest request, PallierType manager) {
       services.updateManager(request.getHeader("X-User"), manager);
    }
    
//    @PUT
//    @Path("upgrade")
//    public void putUpgrade(@Context HttpServletRequest request, PallierType upgrade) {
//       services.updateUpgrade(request.getHeader("X-User"), upgrade);
//    }
//    @PUT
//    @Path("angelUpgrade")
//    public void putangelUpgrade(@Context HttpServletRequest request, PallierType angelUpgrade) {
//       services.updateAngelUpgrade(request.getHeader("X-User"), angelUpgrade);
//    }
}
 
 


