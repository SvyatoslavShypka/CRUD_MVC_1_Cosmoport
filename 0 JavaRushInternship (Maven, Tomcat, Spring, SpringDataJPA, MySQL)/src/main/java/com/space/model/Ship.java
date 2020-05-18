package com.space.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ship")
public class Ship {

//  ID корабля
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//  Название корабля (до 50 знаков включительно)
    @Column
    private String name;

//  Планета пребывания (до 50 знаков включительно)
    @Column
    private String planet;

//  Тип корабля.
//  Если есть переменная Enum, то для корректной её обработки
//  требуется использовать аннотацию @Enumerated
    @Column
    @Enumerated(EnumType.STRING)
    private ShipType shipType;

//  Дата выпуска.
//  Диапазон значений года 2800..3019 включительно
    @Column
    private Date prodDate;

//  Использованный / новый
    @Column
    private Boolean isUsed;

//  Максимальная скорость корабля.
//  Диапазон значений 0,01..0,99 включительно.
//  Используй математическое округление до сотых.
    @Column
    private Double speed;

//  Количество членов экипажа.
//  Диапазон значений 1..9999 включительно.
    @Column
    private Integer crewSize;

//  Рейтинг корабля.
//  Используй математическое округление до сотых.
    @Column
    private Double rating;

//  Entity требуется public / protected конструктор без параметров
    public Ship() {

    }

//  !!!========== Setters and Getters ==========!!!
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean isUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }



//  !!!========== Others methods ==========!!!
    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planet='" + planet + '\'' +
                ", shipType=" + shipType +
                ", prodDate=" + prodDate +
                ", isUsed=" + isUsed +
                ", speed=" + speed +
                ", crewSize=" + crewSize +
                ", rating=" + rating +
                '}';
    }
}
