package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.errors.UnknownBidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.service.impl.BidListServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

class BidListServiceTests {

    private BidListService bidListService;
    private BidListRepository bidListRepository;

    @BeforeEach
    void setup() {
        bidListRepository = Mockito.mock(BidListRepository.class);
        bidListService = new BidListServiceImpl(bidListRepository);
    }

    @Test
    void saveBidList_saveBid_newBid() {
        // GIVEN a new bid
        final var bid = new BidList("Account Test", "Type Test", 10d);

        // WHEN saving the bid
        bidListService.saveBidList(bid);

        // THEN the repository is called once
        Mockito.verify(bidListRepository, Mockito.times(1)).save(bid);
    }

    @Test
    void findBidListById_findBid_existingBid() {
        // GIVEN an existing bid
        final var bid = new BidList("Account Test", "Type Test", 10d);

        // WHEN finding the bid by id
        Mockito.when(bidListRepository.findById(1)).thenReturn(java.util.Optional.of(bid));
        final var returnedBid = bidListService.findBidListById(1);

        // THEN the repository is called once
        Mockito.verify(bidListRepository, Mockito.times(1)).findById(1);
        // AND the returned bid is not empty
        Assertions.assertTrue(returnedBid.isPresent());
    }

    @Test
    void findBidListById_findBid_missingBid() {
        // GIVEN an id
        final var id = 1;

        // WHEN finding the bid by id
        Mockito.when(bidListRepository.findById(id)).thenReturn(Optional.empty());
        final var returnedBid = bidListService.findBidListById(id);

        // THEN the repository is called once
        Mockito.verify(bidListRepository, Mockito.times(1)).findById(1);
        // AND the returned bid is empty
        Assertions.assertTrue(returnedBid.isEmpty());
    }

    @Test
    void updateBidList_updateBid_existingBid() {
        // GIVEN an existing bid
        final var bid = new BidList("Account Test", "Type Test", 10d);

        // WHEN updating the bid
        Mockito.when(bidListRepository.existsById(bid.getBidListId())).thenReturn(true);
        bidListService.updateBidList(bid);

        // THEN the repository is called once
        Mockito.verify(bidListRepository, Mockito.times(1)).save(bid);
    }

    @Test
    void updateBidList_updateBid_unknownBid() {
        // GIVEN an existing bid
        final var bid = new BidList("Account Test", "Type Test", 10d);

        // WHEN updating the bid
        // THEN the repository is called once
        Mockito.when(bidListRepository.existsById(bid.getBidListId())).thenReturn(false);
        Assertions.assertThrows(UnknownBidList.class, () -> {
            bidListService.updateBidList(bid);
        });
    }

    @Test
    void deleteBidList_deleteBid_existingBid() {
        // GIVEN an id
        final var id = 1;

        // WHEN deleting the bid
        Mockito.doNothing().when(bidListRepository).deleteById(id);
        bidListService.deleteBidList(id);

        // THEN the repository is called once
        Mockito.verify(bidListRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void findALlBidList_findAllBidList() {
        // GIVEN no bid

        // WHEN finding all bids
        bidListService.findAllBidList();

        // THEN the repository is called once
        Mockito.verify(bidListRepository, Mockito.times(1)).findAll();
    }
}
