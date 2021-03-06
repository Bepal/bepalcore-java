/*
 * Copyright (c) 2018-2019, BEPAL
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of California, Berkeley nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package pro.bepal.coin.eos.tx.message;

import pro.bepal.categories.ByteArrayData;
import pro.bepal.coin.eos.tx.AccountName;
import pro.bepal.coin.eos.tx.Transaction;
import pro.bepal.util.ByteUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class VoteProducerMessageData implements MessageData {

    public AccountName voter;
    /**
     * 如果为空则不是代理
     */
    public AccountName proxy;
    /**
     * 投票列表需要排序 代码中自动排序
     */
    public List<AccountName> producers = new ArrayList<>();

    @Override
    public byte[] toByte() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            stream.write(voter.accountData);
            stream.write(proxy.accountData);
            stream.write(ByteUtil.uvarToBytes(producers.size()));
            Transaction.sortAccountName(producers);
            for (AccountName producer : producers) {
                stream.write(producer.accountData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream.toByteArray();
    }

    @Override
    public void parse(byte[] dataa) {
        ByteArrayData data = new ByteArrayData(dataa);
        voter = new AccountName(data.readData(8));
        proxy = new AccountName(data.readData(8));
        int count = (int) data.readUVarInt();
        for (int i = 0; i < count; i++) {
            AccountName temp = new AccountName(data.readData(8));
            producers.add(temp);
        }
    }
}
