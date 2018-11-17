/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here we manage the connection between the service and the db
 */

package com.paoloamosso.piggus.dao;

import com.paoloamosso.piggus.model.Transaction;
import com.paoloamosso.piggus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    Transaction findTransactionById(int id_expense);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.user = ?1 " +
            "AND t.isArchived = false " +
            "AND (t.localDate BETWEEN ?2 AND ?3)")
    List<Transaction> findByMonthAndNotArchived(User user, LocalDate startMonth, LocalDate endMonth);

    // List of transactions for the job
    @Query("SELECT t FROM Transaction t " +
            "WHERE t.isRecurrent = true " +
            "AND t.isArchived = false " +
            "AND (t.localDate BETWEEN ?1 AND ?2) " +
            "AND t.recurrentFactor = ?3")
    List<Transaction> findByRecurrentTransactionNotArchivedForMonth(LocalDate startMonth, LocalDate endMonth, int period);

    //Getting the list of month in which the user has a transaction
    @Query(value = "SELECT CAST(EXTRACT(YEAR_MONTH FROM date) AS CHAR(6)) AS ym " +
            "FROM transaction " +
            "WHERE user_user_id = ?1 " +
            "GROUP BY ym " +
            "ORDER BY ym DESC", nativeQuery = true)
    List<String> findMonthListWithTransactions(int userId);
}
