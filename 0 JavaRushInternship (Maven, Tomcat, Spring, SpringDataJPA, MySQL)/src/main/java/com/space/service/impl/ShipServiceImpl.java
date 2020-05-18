package com.space.service.impl;

import com.space.controller.ShipOrder;
import com.space.exceptions.BadRequestException;
import com.space.exceptions.NotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service – это Java класс, который предоставляет с себя основную бизнес-логику.
 * В основном сервис использует готовые DAO / Repositories или же другие сервисы
 * для того чтобы предоставить конечные данные для пользовательского интерфейса.
 * <p>
 * В нашем случае, мы будем реализовывать все методы за счёт методов, представленных
 * в JPARepository. Т.е. наши методы - wrapper над методами JPARepository.
 */

@Service
public class ShipServiceImpl implements ShipService {

    private ShipRepository shipRepository;

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    //  1.1 Получить полный отсортированный список кораблей
    //  в соответствии с переданными фильтрами;
    @Override
    public List<Ship> getSortAllShips(String name, String planet, ShipType shipType,
                                      Long after, Long before, Boolean isUsed,
                                      Double minSpeed, Double maxSpeed, Integer minCrewSize,
                                      Integer maxCrewSize, Double minRating, Double maxRating) {

        List<Ship> sortAllShips = shipRepository.findAll();

        if (name != null) {
            sortAllShips = sortAllShips.stream()
                    .filter(s -> s.getName().contains(name))
                    .collect(Collectors.toList());
        }

        if (planet != null) {
            sortAllShips = sortAllShips.stream()
                    .filter(s -> s.getPlanet().contains(planet))
                    .collect(Collectors.toList());
        }

        if (shipType != null) {
            sortAllShips = sortAllShips.stream()
                    .filter(s -> s.getShipType().equals(shipType))
                    .collect(Collectors.toList());
        }

        if (after != null) {
            sortAllShips = sortAllShips.stream()
                    .filter(ship -> ship.getProdDate().after(new Date(after)))
                    .collect(Collectors.toList());
        }

        if (before != null) {
            sortAllShips = sortAllShips.stream()
                    .filter(ship -> ship.getProdDate().before(new Date(before)))
                    .collect(Collectors.toList());
        }

        if (isUsed != null) {
            sortAllShips = sortAllShips.stream()
                    .filter(ship -> ship.isUsed().equals(isUsed))
                    .collect(Collectors.toList());
        }

        if (minSpeed != null) {
            sortAllShips = sortAllShips.stream()
                    .filter(ship -> ship.getSpeed() >= minSpeed)
                    .collect(Collectors.toList());
        }
        
        if (maxSpeed != null) {
            sortAllShips = sortAllShips.stream()
                    .filter(ship -> ship.getSpeed() <= maxSpeed)
                    .collect(Collectors.toList());
        }

        if (minCrewSize != null) {
            sortAllShips = sortAllShips.stream()
                    .filter(ship -> ship.getCrewSize() >= minCrewSize)
                    .collect(Collectors.toList());
        }

        if (maxCrewSize != null) {
            sortAllShips = sortAllShips.stream()
                    .filter(ship -> ship.getCrewSize() <= maxCrewSize)
                    .collect(Collectors.toList());
        }

        if (minRating != null) {
            sortAllShips = sortAllShips.stream()
                    .filter(ship -> ship.getRating() >= minRating)
                    .collect(Collectors.toList());
        }

        if (maxRating != null) {
            sortAllShips = sortAllShips.stream()
                    .filter(ship -> ship.getRating() <= maxRating)
                    .collect(Collectors.toList());
        }

        return sortAllShips;
    }

    // 1.2 Получить список с эффектом постраничной навигации
    // (Stream: skip + limit) и с определённым порядком (ShipOrder) данных.
    @Override
    public List<Ship> getShipsForPage(List<Ship> sortAllShips, ShipOrder order,
                                      Integer pageNumber, Integer pageSize) {

        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 3;

        return sortAllShips.stream()
                .sorted(getComparator(order))
                .skip(pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    //  2. Создать новый корабль;
    @Override
    public Ship createNewShip(Ship newShip) {

        if (newShip.getName() == null
                || newShip.getPlanet() == null
                || newShip.getShipType() == null
                || newShip.getProdDate() == null
                || newShip.getSpeed() == null
                || newShip.getCrewSize() == null) {

            throw new BadRequestException();
        }

        if (newShip.getName().length() > 50 || newShip.getPlanet().length() > 50
                || newShip.getName().isEmpty() || newShip.getPlanet().isEmpty()
                || newShip.getSpeed() < 0.01d || newShip.getSpeed() > 0.99d
                || newShip.getCrewSize() < 1 || newShip.getCrewSize() > 9999) {

            throw new BadRequestException();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(newShip.getProdDate());
        int year = cal.get(Calendar.YEAR);

        if (year < 2800 || year > 3019) throw new BadRequestException();

        if (newShip.isUsed() == null) {
            newShip.setUsed(false);
        }

        newShip.setRating(shipRatingCalculation(newShip));

        return shipRepository.save(newShip);
    }

    //  3. Редактировать характеристики сущетвующего корабля;
    @Override
    public Ship editShip(Ship newCharacteristics, Long id) {
        // 400
        if (!isThisTheCorrectId(id)) throw new BadRequestException();

        // 404
        if (!shipRepository.existsById(id)) throw new NotFoundException();

        // Получаем ship по id и начинаем править в соответствии с
        // newCharacteristics
        Ship editableShip = getShipById(id);

        // Name
        String name = newCharacteristics.getName();

        if (name != null) {
            if (name.length() > 50 || name.isEmpty()) throw new BadRequestException();

            editableShip.setName(name);
        }

        // Planet
        String planet = newCharacteristics.getPlanet();

        if (planet != null) {
            if (planet.length() > 50 || planet.isEmpty()) throw new BadRequestException();

            editableShip.setPlanet(planet);
        }

        // ShipType
        if (newCharacteristics.getShipType() != null)
            editableShip.setShipType(newCharacteristics.getShipType());

        // isUsed
        if (newCharacteristics.isUsed() != null) {
            editableShip.setUsed(newCharacteristics.isUsed());
        }

        // ProdDate
        if (newCharacteristics.getProdDate() != null) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(newCharacteristics.getProdDate());
            int prodDate = cal.get(Calendar.YEAR);

            if (prodDate < 2800 || prodDate > 3019) throw new BadRequestException();

            editableShip.setProdDate(newCharacteristics.getProdDate());
        }

        // Speed
        Double speed = newCharacteristics.getSpeed();

        if (speed != null) {
            if (speed < 0.01d || speed > 0.99d) throw new BadRequestException();

            editableShip.setSpeed(speed);
        }

        // CrewSize
        Integer crewSize = newCharacteristics.getCrewSize();

        if (crewSize != null) {
            if (crewSize < 1 || crewSize > 9999) throw new BadRequestException();

            editableShip.setCrewSize(crewSize);
        }

        editableShip.setRating(shipRatingCalculation(editableShip));
        return editableShip;
    }

    //  4. Удалить корабль из БД;
    @Override
    public void deleteShipById(Long id) {
        if (!isThisTheCorrectId(id)) throw new BadRequestException();

        if (!shipRepository.existsById(id)) throw new NotFoundException();

        shipRepository.deleteById(id);
    }

    //  5. Получить корабль из БД по id;
    @Override
    public Ship getShipById(Long id) {
        if (!isThisTheCorrectId(id)) throw new BadRequestException();

        if (!shipRepository.existsById(id)) throw new NotFoundException();

        return shipRepository.findById(id).orElse(null);
    }


    // Вспомогательные методы
    // 1.
    private Comparator<Ship> getComparator(ShipOrder order) {
        if (order == null) {
            return Comparator.comparing(Ship::getId);
        }

        Comparator<Ship> comparator = null;
        switch (order.getFieldName()) {
            case "id":
                comparator = Comparator.comparing(Ship::getId);
                break;
            case "speed":
                comparator = Comparator.comparing(Ship::getSpeed);
                break;
            case "prodDate":
                comparator = Comparator.comparing(Ship::getProdDate);
                break;
            case "rating":
                comparator = Comparator.comparing(Ship::getRating);
        }

        return comparator;
    }

    // 2. Расчёт рейтинга корабля
    private Double shipRatingCalculation(Ship ship) {
        double speed = ship.getSpeed();
        double coefficient = ship.isUsed() ? 0.5d : 1.0d;

        Calendar cal = Calendar.getInstance();
        cal.setTime(ship.getProdDate());
        double y1 = cal.get(Calendar.YEAR);

        double result = (80 * speed * coefficient) / (3019 - y1 + 1);
        return (double) Math.round(result * 100) / 100;
    }

    // 3. Проверяем корректность входящего id
    private Boolean isThisTheCorrectId(Long id) {
        return id != null &&
                id > 0 &&
                id == Math.floor(id);
    }

}
