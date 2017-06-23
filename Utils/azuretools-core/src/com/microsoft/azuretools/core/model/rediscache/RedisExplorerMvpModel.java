/**
 * Copyright (c) Microsoft Corporation
 * <p/>
 * All rights reserved.
 * <p/>
 * MIT License
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.microsoft.azuretools.core.model.rediscache;

import static redis.clients.jedis.ScanParams.SCAN_POINTER_START;

import com.microsoft.azuretools.azurecommons.util.Utils;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.exceptions.JedisException;

public class RedisExplorerMvpModel {

    private static final int DEFAULT_REDIS_DB_NUMBER = 16;
    private static final int DEFAULT_COUNT = 50;
    private static final String DEFAULT_SCAN_PATTERN = "*";

    private RedisExplorerMvpModel() {
    }

    private static final class RedisExplorerMvpModelHolder {
        private static final RedisExplorerMvpModel INSTANCE = new RedisExplorerMvpModel();
    }

    public static RedisExplorerMvpModel getInstance() {
        return RedisExplorerMvpModelHolder.INSTANCE;
    }

    /**
     * Get the number of databases the Redis Cache has.
     * 
     * @param sid
     *            subscription id of Redis Cache
     * @param id
     *            resource id of Redis Cache
     * @return the number of databases the Redis Cache has
     * @throws Exception Error getting the Redis Cache
     */
    public int getDbNumber(String sid, String id) throws Exception {
        int dbNum = DEFAULT_REDIS_DB_NUMBER;
        try (Jedis jedis = RedisConnectionPools.getInstance().getJedis(sid, id)) {
            try {
                List<String> dbs = jedis.configGet("databases");
                if (dbs.size() > 0) {
                    dbNum = Integer.parseInt(dbs.get(1));
                }
            } catch (JedisException e) {
                // TODO: keep ping to different db index to figure out how many dbs
                // the redis has.
            }
        } 
        return dbNum;
    }
    
    /**
     * 
     * @param sid
     *            subscription id of Redis Cache
     * @param id
     *            resource id of Redis Cache
     * @param db
     *            index of Redis Cache database
     * @param cursor
     *            cursor for Redis Scan command
     * @param pattern
     *            pattern for Redis Scan Param
     * @return Scan Result returned from Jedis
     * @throws Exception
     * 
     */
    public ScanResult<String> scanKeys(String sid, String id, int db, String cursor, String pattern) throws Exception {
        if (Utils.isEmptyString(cursor)) {
            cursor = SCAN_POINTER_START;
        }
        if (Utils.isEmptyString(pattern)) {
            pattern = DEFAULT_SCAN_PATTERN;
        }
        try (Jedis jedis = RedisConnectionPools.getInstance().getJedis(sid, id)) {
            jedis.select(db);
            return jedis.scan(cursor, new ScanParams().match(pattern).count(DEFAULT_COUNT));
        }
    }
}