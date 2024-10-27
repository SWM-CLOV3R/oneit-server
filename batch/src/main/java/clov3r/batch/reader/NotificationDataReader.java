package clov3r.batch.reader;

import clov3r.batch.common.dto.NotificationEventDTO;
import clov3r.batch.reader.expression.Expression;
import clov3r.batch.reader.options.QuerydslNoOffsetNumberOptions;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class NotificationDataReader extends RepositoryItemReader<NotificationEventDTO> {

  private final EntityManagerFactory emf;
//
//  @Bean
//  @StepScope
//  public QuerydslNoOffsetPagingItemReader<NotificationEventDTO> sendNotificationReader() {
//    System.out.println("SendNotificationBatch.sendNotificationReader");
//    QuerydslNoOffsetPagingItemReader<NotificationEventDTO> options = QuerydslNoOffsetNumberOptions.of(notification.id, Expression.ASC, "notificationId");
//  }
//
//  private ConstructorExpression<NotificationEventDTO> createConstructorExpression() {
//    return Projections.constructor(
//        NotificationEventDTO.class,
//        notification.id,
//        notification.title,
//        notification.content,
//        notification.notiStatus
//    );
//  }
}
