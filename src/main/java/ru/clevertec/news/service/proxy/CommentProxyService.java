package ru.clevertec.news.service.proxy;

import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.clevertec.news.cache.Cache;
import ru.clevertec.news.dto.CommentDto;

@Aspect
@AllArgsConstructor
public class CommentProxyService {

    private final Cache<Long, CommentDto> cache;
    private static final Logger logger = LoggerFactory.getLogger(CommentProxyService.class);

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Pointcut("execution(* ru.clevertec.news.service.impl.CommentServiceImpl.findById(..)))")
    public void getMethod() {

    }

    @Pointcut("execution(* ru.clevertec.news.service.impl.CommentServiceImpl.create(..)))")
    public void createMethod() {

    }

    @Pointcut("execution(* ru.clevertec.news.service.impl.CommentServiceImpl.update(..)))")
    public void updateMethod() {

    }

    @Pointcut("execution(* ru.clevertec.news.service.impl.CommentServiceImpl.delete(..)))")
    public void deleteMethod() {

    }

    /**
     * Возвращает объект CommentDto по указанному идентификатору.
     * Если объект есть в кэше, метод возвращает его из кэша.
     * В противном случае, вызывает метод из оригинального сервиса и кэширует результат.
     *
     * @param pjp прокси-объект для вызова оригинального метода
     * @return объект CommentDto
     * @throws Throwable при возникновении исключения при выполнении оригинального метода
     */
    @Around("getMethod()")
    public Object doGet(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("Proxy comments aop: get method");

        Long id = (Long) pjp.getArgs()[0];
        if (cache.get(id) == null) {
            CommentDto commentDto = (CommentDto) pjp.proceed();
            cache.put(id, commentDto);
            return commentDto;
        } else {
            return cache.get(id);
        }
    }

    /**
     * Выполняет создание объекта с кэшированием результата.
     * После выполнения метода, результат добавляется в кэш.
     *
     * @param pjp прокси-объект для вызова оригинального метода
     * @return объект, созданный методом, с кэшированным результатом
     * @throws Throwable если возникла ошибка при выполнении оригинального метода
     */
    @Around("createMethod()")
    public Object doCreate(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("Proxy comments aop: post method");

        CommentDto commentDto = (CommentDto) pjp.proceed();
        cache.put(commentDto.getId(), commentDto);
        return commentDto;
    }

    /**
     * Выполняет обновление объекта с кэшированием результата.
     * После выполнения метода, результат обновления добавляется в кэш.
     *
     * @param pjp прокси-объект для вызова оригинального метода
     * @return объект, обновленный методом, с кэшированным результатом
     * @throws Throwable если возникла ошибка при выполнении оригинального метода
     */
    @Around("updateMethod()")
    public Object doUpdate(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("Proxy comments aop: update method");

        CommentDto commentDto = (CommentDto) pjp.proceed();
        cache.put(commentDto.getId(), commentDto);
        return commentDto;
    }

    /**
     * Выполняет удаление объекта с кэшированием результата.
     * После выполнения метода, соответствующий объект удаляется из кэша.
     *
     * @param pjp прокси-объект для вызова оригинального метода
     * @return идентификатор удаленного объекта
     * @throws Throwable если возникла ошибка при выполнении оригинального метода
     */
    @Around("deleteMethod()")
    public Object doDelete(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("Proxy comments aop: delete method");

        Long id = (Long) pjp.getArgs()[0];
        pjp.proceed();
        cache.remove(id);
        return id;
    }
}
