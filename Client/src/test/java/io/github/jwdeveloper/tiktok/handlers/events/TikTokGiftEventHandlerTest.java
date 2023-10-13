/*
 * Copyright (c) 2023-2023 jwdeveloper jacekwoln@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.jwdeveloper.tiktok.handlers.events;

import io.github.jwdeveloper.tiktok.data.events.gift.TikTokGiftEvent;
import io.github.jwdeveloper.tiktok.data.models.Picture;
import io.github.jwdeveloper.tiktok.gifts.TikTokGiftManager;
import io.github.jwdeveloper.tiktok.messages.data.GiftStruct;
import io.github.jwdeveloper.tiktok.messages.data.Image;
import io.github.jwdeveloper.tiktok.messages.data.User;
import io.github.jwdeveloper.tiktok.messages.webcast.WebcastGiftMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TikTokGiftEventHandlerTest {

    public static TikTokGiftEventHandler handler;


    @BeforeAll
    public void before() {
        var manager = new TikTokGiftManager();
        manager.registerGift(123, "example", 123, new Picture("image.webp"));
        handler = new TikTokGiftEventHandler(manager);
    }

    @Test
    void shouldHandleGifts() {
        var message = getGiftMessage("example-new-name", 123, "image-new.png", 0, 1);
        var result = handler.handleGift(message);

        Assertions.assertEquals(1, result.size());

        var event = (TikTokGiftEvent) result.get(0);
        var gift = event.getGift();
        Assertions.assertEquals("image-new.png",gift.getPicture().getLink());
        Assertions.assertEquals(123,gift.getId());
    }


    public WebcastGiftMessage getGiftMessage(String giftName,
                                             int giftId,
                                             String giftImage,
                                             int sendType,
                                             int userId) {
        var builder = WebcastGiftMessage.newBuilder();
        var giftBuilder = GiftStruct.newBuilder();
        var userBuilder = User.newBuilder();


        giftBuilder.setId(giftId);
        giftBuilder.setName(giftName);
        giftBuilder.setImage(Image.newBuilder().addUrlList(giftImage).build());
        userBuilder.setId(userId);

        builder.setGiftId(giftId);
        builder.setUser(userBuilder);
        builder.setSendType(sendType);
        builder.setGift(giftBuilder);
        return builder.build();
    }

}