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
package pro.bepal.coin.eos.abi;

import org.spongycastle.util.encoders.Hex;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import pro.bepal.categories.ByteArrayData;
import pro.bepal.coin.eos.tx.AccountName;
import pro.bepal.coin.eos.tx.Transaction;
import pro.bepal.coin.eos.tx.message.Asset;

public class ActionHexData {

    public JSONObject fromHex(byte[] hex, StructDef struct) {
        JSONObject json = new JSONObject();
        ByteArrayData data = new ByteArrayData(hex);
        for (int i = 0; i < struct.fields.size(); i++) {
            FieldDef field = struct.fields.get(i);
            json.put(field.name, toFieldString(data, field));
        }
        return json;
    }

    public byte[] toHex(JSONObject json, StructDef struct) {
        ByteArrayData data = new ByteArrayData();
        try {
            for (int i = 0; i < struct.fields.size(); i++) {
                FieldDef field = struct.fields.get(i);
                fromFieldString(data, json, field);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data.toBytes();
    }

    private String toFieldString(ByteArrayData data, FieldDef field) {
        if ("account_name".equals(field.type)) {
            return new AccountName(data.readData(8)).toString();
        } else if ("name".equals(field.type)) {
            return new AccountName(data.readData(8)).toString();
        } else if ("asset".equals(field.type)) {
            Asset asset = new Asset();
            asset.parse(data);
            return asset.toString();
        } else if ("string".equals(field.type)) {
            return data.readStringByUVar();
        } else if ("checksum256".equals(field.type)) {
            return Hex.toHexString(data.readData(32));
        } else if ("signature".equals(field.type)) {
            data.readByte();
            return Transaction.toEOSSignature(data.readData(65));
        }
        throw new RuntimeException("不支持Abi  " + field.type);
    }

    private void fromFieldString(ByteArrayData data, JSONObject json, FieldDef field) throws JSONException {
        String value = json.getString(field.name);
        if ("account_name".equals(field.type)) {
            data.putBytes(new AccountName(value).accountData);
        } else if ("name".equals(field.type)) {
            data.putBytes(new AccountName(value).accountData);
        } else if ("asset".equals(field.type)) {
            Asset asset = new Asset(value);
            asset.toByte(data);
        } else if ("string".equals(field.type)) {
            data.appendStringByUVar(value);
        } else if ("checksum256".equals(field.type)) {
            data.putBytes(Hex.decode(value));
        } else if ("signature".equals(field.type)) {
            data.putBytes(Transaction.fromEOSSignature(value));
        } else {
            throw new RuntimeException("不支持Abi" + field.type);
        }
    }

    public JSONObject getAbi(String contract) throws JSONException {
//        String path = "D:\\eosabi\\";
//        String abi = path + contract + ".txt";
//        if (FileInfo.fileIsExists(abi)) {
//            return new JSONObject(FileInfo.getFileString(abi));
//        }
//        String data = ServerInfo.getString("https://eospark.com/api/contract/" + contract + "/info");
//        JSONObject json = new JSONObject(data);
//        String jsonabi = json.getJSONObject("data").getString("abi_raw");
//        JSONObject jsonObject = new JSONObject(jsonabi);
//        FileInfo.setFileString(jsonObject.toString(4), abi);
        return null;
    }

    public StructDef getFun(String name, AbiDef def) {
        for (int i = 0; i < def.structs.size(); i++) {
            if (def.structs.get(i).name.equals(name)) {
                return def.structs.get(i);
            }
        }
        return null;
    }

    public JSONObject getAction(JSONObject json) {
        try {
//            if (json.has("data")) {
//                return json;
//            }
            JSONObject abijson = getAbi(json.getString("account"));
            AbiDef def = new AbiDef();
            def.parse(abijson);
            StructDef struct = getFun(json.getString("name"), def);
            byte[] data = Hex.decode(json.getString("hex_data"));
            return fromHex(data, struct);
        } catch (Exception ex) {
            ex.printStackTrace();
            return json;
        }
    }
}
