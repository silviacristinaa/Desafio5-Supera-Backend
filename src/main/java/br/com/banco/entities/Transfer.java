package br.com.banco.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "transferencia")
public class Transfer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "data_transferencia", nullable = false)
	private LocalDateTime transferDate;
	@Column(name = "valor", nullable = false)
	private BigDecimal value;
	@Column(name = "tipo", nullable = false, length = 15)
	private String type;
	@Column(name = "nome_operador_transacao", length = 50)
	private String transactionOperatorName;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conta_id", insertable = false, updatable = false)
	private Account account;
}
