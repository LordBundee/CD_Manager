/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdmanager;

/**
 *
 * @author Troy
 */
public interface IChatHost
{
    void ConnectToServer(String server, int port);
    void HandleReply(String msg);
}
