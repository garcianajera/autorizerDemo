package com.demo.repository;

import com.demo.exception.RepositoryException;
import com.demo.model.BaseEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BaseGenericRepositoryTest {

    @Test
    void testSave() {
        BaseGenericRepository<BaseEntity> genericRepository = new BaseGenericRepository<BaseEntity>() {
        };
        BaseEntity entity = createBaseEntity();
        assertNull(entity.getId());
        // id before save is null
        BaseEntity baseEntity = genericRepository.save(entity);
        assertNotNull(baseEntity);
        assertNotNull(baseEntity.getId());
    }

    @Test
    void testSaveExistingEntity() {
        BaseGenericRepository<BaseEntity> genericRepository = new BaseGenericRepository<BaseEntity>() {
        };

        BaseEntity entity = createBaseEntity();
        assertNull(entity.getId());
        // id before save is null
        BaseEntity storedEntity = genericRepository.save(entity);
        assertNotNull(storedEntity);
        assertNotNull(storedEntity.getId());

        UUID id = storedEntity.getId();
        //if we save an entity that already has id, repository should not change it
        BaseEntity storedEntity2 = genericRepository.save(entity);
        assertEquals(id, storedEntity2.getId());

    }

    @Test
    void testSaveExceptionNullEntity() {
        BaseGenericRepository<BaseEntity> genericRepository = new BaseGenericRepository<BaseEntity>() {
        };
        Exception exception = assertThrows(RepositoryException.class, () -> genericRepository.save(null));

        assertEquals("Entity should not be null", exception.getMessage());
    }

    @Test
    void testFindById() {

        BaseGenericRepository<BaseEntity> genericRepository = new BaseGenericRepository<BaseEntity>() {
        };

        BaseEntity entity = createBaseEntity();
        BaseEntity expectedEntity = genericRepository.save(entity);
        UUID id = expectedEntity.getId();

        BaseEntity findEntity = genericRepository.findById(id);

        assertEquals(expectedEntity, findEntity);
    }

    @Test
    void testFindAll() {

        BaseGenericRepository<BaseEntity> genericRepository = new BaseGenericRepository<BaseEntity>() {
        };

        genericRepository.save(createBaseEntity());
        genericRepository.save(createBaseEntity());
        assertEquals(2, genericRepository.findAll().size());
    }


    private BaseEntity createBaseEntity() {
        return new BaseEntity() {
        };
    }
}
