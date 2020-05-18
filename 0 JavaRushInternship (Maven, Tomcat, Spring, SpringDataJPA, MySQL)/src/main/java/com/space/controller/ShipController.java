package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.impl.ShipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/rest/ships")
public class ShipController {
    private final ShipServiceImpl shipServiceImpl;

    @Autowired
    public ShipController(ShipServiceImpl shipServiceImpl) {
        this.shipServiceImpl = shipServiceImpl;
    }

    @GetMapping
    public List<Ship> getSortAllShips(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String planet,
                                  @RequestParam(required = false) ShipType shipType,
                                  @RequestParam(required = false) Long after,
                                  @RequestParam(required = false) Long before,
                                  @RequestParam(required = false) Boolean isUsed,
                                  @RequestParam(required = false) Double minSpeed,
                                  @RequestParam(required = false) Double maxSpeed,
                                  @RequestParam(required = false) Integer minCrewSize,
                                  @RequestParam(required = false) Integer maxCrewSize,
                                  @RequestParam(required = false) Double minRating,
                                  @RequestParam(required = false) Double maxRating,
                                  @RequestParam(required = false) ShipOrder order,
                                  @RequestParam(required = false) Integer pageNumber,
                                  @RequestParam(required = false) Integer pageSize) {

        List <Ship> ships = shipServiceImpl.getSortAllShips(name, planet, shipType,
                                                            after, before, isUsed,
                                                            minSpeed, maxSpeed, minCrewSize,
                                                            maxCrewSize, minRating, maxRating);

        return shipServiceImpl.getShipsForPage(ships, order, pageNumber, pageSize);
    }

    @GetMapping("/count")
    public Integer getCountSortAllShips(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) String planet,
                                        @RequestParam(required = false) ShipType shipType,
                                        @RequestParam(required = false) Long after,
                                        @RequestParam(required = false) Long before,
                                        @RequestParam(required = false) Boolean isUsed,
                                        @RequestParam(required = false) Double minSpeed,
                                        @RequestParam(required = false) Double maxSpeed,
                                        @RequestParam(required = false) Integer minCrewSize,
                                        @RequestParam(required = false) Integer maxCrewSize,
                                        @RequestParam(required = false) Double minRating,
                                        @RequestParam(required = false) Double maxRating) {

        return shipServiceImpl.getSortAllShips(name, planet, shipType,
                after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize,
                maxCrewSize, minRating, maxRating).size();
    }

    @GetMapping("/{id}")
    public Ship getShipById(@PathVariable Long id) {
        return shipServiceImpl.getShipById(id);
    }

    @PostMapping
    @ResponseBody
    public Ship createNewShip(@RequestBody Ship newShip) {
        return shipServiceImpl.createNewShip(newShip);
    }

    @PostMapping("/{id}")
    @ResponseBody
    public Ship editShip(@RequestBody Ship newCharacteristics, @PathVariable Long id) {
        return shipServiceImpl.editShip(newCharacteristics, id);
    }

    @DeleteMapping("/{id}")
    public void deleteShipById(@PathVariable Long id) {
        shipServiceImpl.deleteShipById(id);
    }

}
