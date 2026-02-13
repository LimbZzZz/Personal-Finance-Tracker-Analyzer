package org.example.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.CustomException.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BaseService<T, ID>{
    protected final JpaRepository<T, ID> repository;

    public T create(T entity){
        log.debug("Создание сущности {}", entity);
        long currentTime = System.currentTimeMillis();

        T saved = repository.save(entity);

        log.debug("Сущность {} создана за {}мс", saved, System.currentTimeMillis() - currentTime);
        return saved;
    }

    public T findById(ID id){
        log.debug("Поиск сущности по id: {}", id);
        long start = System.currentTimeMillis();

        T entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сущность с ID " + id + " не найдена"));

        log.debug("Сущность {} найдена за {} мс", entity,
                System.currentTimeMillis() - start);
        return entity;
    }

    public List<T> findAll() {
        log.debug("Получение всех сущностей");
        long start = System.currentTimeMillis();

        List<T> entities = repository.findAll();

        log.debug("Найдено {} сущностей за {} мс", entities.size(),
                System.currentTimeMillis() - start);
        return entities;
    }

    public void delete(ID id) {
        log.debug("Удаление сущности с id: {}", id);
        long start = System.currentTimeMillis();

        T entity = findById(id);
        repository.delete(entity);

        log.debug("Сущность {} удалена за {} мс", entity,
                System.currentTimeMillis() - start);
    }
}
