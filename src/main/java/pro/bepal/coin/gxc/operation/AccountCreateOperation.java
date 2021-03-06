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

import org.json.me.JSONObject;
import pro.bepal.categories.ByteArrayData;
import pro.bepal.coin.gxc.UserAccount;

public class AccountCreateOperation extends BaseOperation {

    public static final String KEY_REGISTRAR = "registrar";
    public static final String KEY_REFERRER = "referrer";
    public static final String KEY_REFERRER_PERCENT = "referrer_percent";
    public static final String KEY_NAME = "name";
    public static final String KEY_OWNER = "owner";
    public static final String KEY_ACTIVE = "active";
    public static final String KEY_OPTIONS = "options";

    public UserAccount registrar;

    public UserAccount referrer;

    // 最大推荐返佣值
    // https://block.gxb.io/#/transaction/ae516aa364c0c790b58e2bcdb8a370ebc25bb882
    private short referrerPercent = 10000;

    public String name;

    public Authority owner;

    public Authority active;

    public AccountOptions options;

    public AccountCreateOperation() {
        super(OperationType.ACCOUNT_CREATE_OPERATION);
    }

    @Override
    public byte[] toByte() {
        ByteArrayData data = new ByteArrayData();
        data.putBytes(fee.toByte());
        data.putBytes(registrar.toByte());
        data.putBytes(referrer.toByte());
        data.appendShort(referrerPercent);
        data.appendString(name);
        data.putBytes(owner.toByte());
        data.putBytes(active.toByte());
        data.putBytes(options.toByte());
        data.putBytes(extensions.toByte());
        return data.toBytes();
    }

    @Override
    public void fromJson(JSONObject obj) {
        try {
            registrar = new UserAccount(obj.getString(KEY_REGISTRAR));

        } catch (Exception ex) {
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put(KEY_REGISTRAR, registrar.getId());
        json.put(KEY_REFERRER, referrer.getId());
        json.put(KEY_REFERRER_PERCENT, referrerPercent);
        json.put(KEY_NAME, name);
        json.put(KEY_OWNER, owner.toJson());
        json.put(KEY_ACTIVE, active.toJson());
        json.put(KEY_OPTIONS, options.toJson());
        return json;
    }
}
