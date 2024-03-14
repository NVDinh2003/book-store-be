package com.nvd.bookstore.service;

import com.nvd.bookstore.entity.Product;
import com.nvd.bookstore.entity.Publisher;
import com.nvd.bookstore.exception.ResourceNotFoundException;
import com.nvd.bookstore.payload.request.PublisherRequest;
import com.nvd.bookstore.repository.ProductRepository;
import com.nvd.bookstore.repository.PublisherRepository;
import com.nvd.bookstore.untils.AppConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    public Publisher getPublisherById(Long id) {
        if (publisherRepository.findById(id).isPresent()) {
            return publisherRepository.findById(id).get();
        } else throw new ResourceNotFoundException(AppConstant.PUBLISHER_NOT_FOUND + id);
    }

    public List<Product> getBooksByPublisherId(Long id) {
        if (publisherRepository.findById(id).isPresent()) {
            return productRepository.findAllByPublisherId(id);
        } else throw new ResourceNotFoundException(AppConstant.PUBLISHER_NOT_FOUND + id);
    }

    public Publisher addPublisher(PublisherRequest publisherRequest) {
        Publisher publisher = modelMapper.map(publisherRequest, Publisher.class);
        return publisherRepository.save(publisher);
    }

    public Publisher updatePublisher(Long id, PublisherRequest publisherRequest) {
        Publisher oldPublisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.PUBLISHER_NOT_FOUND + id));

        oldPublisher.setName(publisherRequest.getName());
        oldPublisher.setDeleted(publisherRequest.isDeleted());

        return publisherRepository.save(oldPublisher);
    }

    public void deletePublisher(Long id) {
        Publisher publisherToDelete = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.PUBLISHER_NOT_FOUND + id));
        publisherToDelete.setDeleted(false);
        publisherRepository.save(publisherToDelete);
    }
}
