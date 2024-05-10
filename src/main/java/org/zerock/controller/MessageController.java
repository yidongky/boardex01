package org.zerock.controller;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.MessageVO;
import org.zerock.service.MessageService;

/*
 * @RestController 뷰를 만들어 내지 않고 데이터 자체를 변환 JSON,xml
 * @ResponseEntity 결과 데이터 + HTTP의 상태코드
 * @RequestBody  @ModelAttribute 유사하지만 JSON에서사용됨
 * @PathVariable URI 원하는 데이터 추룰 (파라메터 값 등 )
 */

@RestController
@RequestMapping("/messages")
public class MessageController {

  @Inject
  private MessageService service;

  @RequestMapping(value = "/", method = RequestMethod.POST)
  public ResponseEntity<String> addMessage(@RequestBody MessageVO vo) {

    ResponseEntity<String> entity = null;
    try {
      service.addMessage(vo);
      entity = new ResponseEntity<>("success", HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      entity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return entity;
  }

}

