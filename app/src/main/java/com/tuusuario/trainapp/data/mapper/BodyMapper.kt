package com.tuusuario.trainapp.data.mapper

import com.tuusuario.trainapp.data.db.entities.BodyWeight
import com.tuusuario.trainapp.data.db.entities.GlobalSensation
import com.tuusuario.trainapp.domain.model.BodyWeight as DomainBodyWeight
import com.tuusuario.trainapp.domain.model.GlobalSensation as DomainGlobalSensation

object BodyMapper {

    fun toDomain(entity: BodyWeight): DomainBodyWeight {
        return DomainBodyWeight(
            id = entity.id,
            date = entity.date,
            weightKg = entity.weightKg,
            photoUri = entity.photoUri,
            notes = entity.notes
        )
    }

    fun toEntity(domain: DomainBodyWeight): BodyWeight {
        return BodyWeight(
            id = domain.id,
            date = domain.date,
            weightKg = domain.weightKg,
            photoUri = domain.photoUri,
            notes = domain.notes
        )
    }

    fun toDomain(entity: GlobalSensation): DomainGlobalSensation {
        return DomainGlobalSensation(
            id = entity.id,
            date = entity.date,
            sleep = entity.sleep,
            fatigue = entity.fatigue,
            motivation = entity.motivation,
            pain = entity.pain,
            painNotes = entity.painNotes,
            notes = entity.notes,
            isStart = entity.isStart
        )
    }

    fun toEntity(domain: DomainGlobalSensation): GlobalSensation {
        return GlobalSensation(
            id = domain.id,
            date = domain.date,
            sleep = domain.sleep,
            fatigue = domain.fatigue,
            motivation = domain.motivation,
            pain = domain.pain,
            painNotes = domain.painNotes,
            notes = domain.notes,
            isStart = domain.isStart
        )
    }
}