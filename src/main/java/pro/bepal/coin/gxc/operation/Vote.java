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
package pro.bepal.coin.gxc.operation;

import pro.bepal.categories.ByteArrayData;
import pro.bepal.coin.gxc.GXCSerializable;

public class Vote {

    private int type;
    private int instance;

    public Vote(String vote){
        String[] parts = vote.split(":");
        assert(parts.length == 2);
        this.type = Integer.valueOf(parts[0]);
        this.instance = Integer.valueOf(parts[1]);
    }

    public Vote(int type, int instance){
        this.type = type;
        this.instance = instance;
    }

    public byte[] toByte() {
        ByteArrayData data = new ByteArrayData();
        data.appendByte(type);
        data.appendShort(this.instance);
        data.appendByte(0);
        return data.toBytes();
//        return new byte[] { (byte) this.instance, (byte) this.type };
    }

    @Override
    public String toString() {
        return String.format("%d:%d", this.type, this.instance);
    }
}
