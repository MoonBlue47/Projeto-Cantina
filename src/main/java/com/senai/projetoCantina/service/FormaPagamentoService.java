package com.senai.projetoCantina.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senai.projetoCantina.dto.FormaPagamentoDto;
import com.senai.projetoCantina.model.FormaPagamento;
import com.senai.projetoCantina.repository.FormaPagamentoRepository;

@Service
public class FormaPagamentoService {
	@Autowired
    private FormaPagamentoRepository repository;

    
    @Transactional(readOnly = true)
    public List<FormaPagamentoDto> findAll() {
        List<FormaPagamento> list = repository.findAll();
        return list.stream().map(FormaPagamentoDto::new).collect(Collectors.toList());
    }

    
    @Transactional(readOnly = true)
    public FormaPagamentoDto findById(Long id) {
        FormaPagamento entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada. ID: " + id));
        return new FormaPagamentoDto(entity);
    }


    @Transactional
    public FormaPagamentoDto insert(FormaPagamentoDto dto) {
        FormaPagamento entity = new FormaPagamento();
        
       
        if (dto.getTipo() != null) {
            entity.setTipo(FormaPagamento.TipoPagamento.valueOf(dto.getTipo().toUpperCase()));
        }
        
        entity = repository.save(entity);
        return new FormaPagamentoDto(entity);
    }

   
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Forma de pagamento não encontrada para exclusão. ID: " + id);
        }
        repository.deleteById(id);
    }
}

