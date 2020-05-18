package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import javax.transaction.Transactional;
import java.util.List;

/** Здесь мы указываем основные методы, при помощи которых
 *  будет реализована бизнес-логика проекта.
 *  По сути, это набор методов, которые нам нужно обязательно
 *  реализовать в отношении нашего POJO.
 *  Plain Old Java Object (POJO) - старый добрый Java-объект.
 *  Всего будет 5 методов.*/

public interface ShipService {
    //  1.1 Получить полный отсортированный список кораблей
    //  в соответствии с переданными фильтрами.
    //  Этот метод нам ещё пригодится, когда мы будем предоставлять
    //  количество кораблей.
    List<Ship> getSortAllShips(String name, String planet, ShipType shipType,
                               Long after, Long before, Boolean isUsed,
                               Double minSpeed, Double maxSpeed, Integer minCrewSize,
                               Integer maxCrewSize, Double minRating, Double maxRating);

    // 1.2 Получить список с эффектом постраничной навигации
    // (Stream: skip + limit) и с определённым порядком данных.
    List<Ship> getShipsForPage(List<Ship> sortAllShips, ShipOrder order,
                               Integer pageNumber, Integer pageSize);

    //  2. Создать новый корабль;
    Ship createNewShip(Ship newShip);

    //  3. Редактировать характеристики сущетвующего корабля;
    Ship editShip(Ship newCharacteristics, Long id);

    //  4. Удалить корабль из БД;
    void deleteShipById(Long id);

    //  5. Получить корабль из БД по id;
    Ship getShipById(Long id);

}