package com.donzelitos.multtenancy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CDT_FROTA_X_PLACA")
@SequenceGenerator(name = "SEQ_CDT_FROTA_X_PLACA", sequenceName = "SEQ_CDT_FROTA_X_PLACA", allocationSize = 1)
public class FrotaPlaca {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CDT_FROTA_X_PLACA")
    @Column(name = "CD_ID")
    private Long cdId;
    @Column(name = "FROTA")
    private String frota;
    @Column(name = "PLACA")
    private String placa;


}